package cep;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class CepProDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);
        UserAction ac1 = new UserAction(1001l, "logout", 1000L);
        UserAction ac2 = new UserAction(1001l, "logout", 3000L);
        UserAction ac4 = new UserAction(1001l, "logout", 2000L);
        UserAction ac5 = new UserAction(1002l, "logout", 1000L);
        UserAction ac6 = new UserAction(1002l, "logout", 2000L);
        DataStreamSource<UserAction> actions = env.fromElements(ac1, ac2,ac4, ac5, ac6);
        KeyedStream<UserAction, Long> keyed =
                actions.assignTimestampsAndWatermarks(WatermarkStrategy.<UserAction>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                        .withTimestampAssigner(new SerializableTimestampAssigner<UserAction>() {
                            @Override
                            public long extractTimestamp(UserAction userAction, long l) {
                                return userAction.getTs();
                            }
                        }))
                        .keyBy(value -> value.getUserId());
        Pattern<UserAction, UserAction> pattern = Pattern.<UserAction>begin("fail")
                .where(new SimpleCondition<UserAction>() {
                    @Override
                    public boolean filter(UserAction userAction) throws Exception {
                        return userAction.getUserAction().equals("logout");
                    }
                }).times(3).consecutive();

        PatternStream<UserAction> patternStream = CEP.pattern(keyed, pattern);

        patternStream.process(new PatternProcessFunction<UserAction, String>() {
            @Override
            public void processMatch(Map<String, List<UserAction>> map, Context context, Collector<String> collector) throws Exception {
                UserAction first = map.get("fail").get(0);
                UserAction second = map.get("fail").get(1);
                UserAction third = map.get("fail").get(2);
                collector.collect(first.getUserId() + "" + first.getTs() + "," + second.getTs() + "," + third.getTs());
            }
        }).print();

        env.execute();
    }
}
