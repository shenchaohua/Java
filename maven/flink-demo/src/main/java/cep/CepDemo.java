package cep;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class CepDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);
        UserAction ac1 = new UserAction(1001l, "login", 1000L);
        UserAction ac2 = new UserAction(1001l, "pay", 3000L);
        UserAction ac4 = new UserAction(1001l, "logout", 2000L);
        UserAction ac5 = new UserAction(1002l, "login", 1000L);
        UserAction ac6 = new UserAction(1002l, "logout", 2000L);
        UserAction ac3 = new UserAction(1002l, "car", 3000L);
        DataStreamSource<UserAction> actions = env.fromElements(ac1, ac2, ac3,ac4, ac5, ac6);
        KeyedStream<UserAction, Long> keyed =
                actions.assignTimestampsAndWatermarks(WatermarkStrategy.<UserAction>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                        .withTimestampAssigner(new SerializableTimestampAssigner<UserAction>() {
                            @Override
                            public long extractTimestamp(UserAction userAction, long l) {
                                return userAction.getTs();
                            }
                        }))
                        .keyBy(value -> value.getUserId());
        Pattern<UserAction, UserAction> pattern = Pattern.<UserAction>begin("first")
                .where(new SimpleCondition<UserAction>() {
                    @Override
                    public boolean filter(UserAction userAction) throws Exception {
                        return userAction.getUserAction().equals("login");
                    }
                }).next("second").where(
                        new SimpleCondition<UserAction>() {
                            @Override
                            public boolean filter(UserAction userAction) throws Exception {
                                return userAction.getUserAction().equals("logout");
                            }
                        }
                );
        PatternStream<UserAction> patternStream = CEP.pattern(keyed, pattern);
        patternStream.select(new PatternSelectFunction<UserAction, String>() {
            @Override
            public String select(Map<String, List<UserAction>> map) throws Exception {
                UserAction first = map.get("first").get(0);
                UserAction second = map.get("second").get(0);
                return first.getUserId() + "match pattern";
            }
        }).print();
        env.execute();
    }
}
