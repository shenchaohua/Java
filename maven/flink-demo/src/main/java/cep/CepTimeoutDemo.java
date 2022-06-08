package cep;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.functions.TimedOutPartialMatchHandler;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class CepTimeoutDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        OrderEvent ac1 = new OrderEvent("user_1","order_1","create", 1000L);
        OrderEvent ac2 = new OrderEvent("user_2","order_2","create", 1000L);
        OrderEvent ac5 = new OrderEvent("user_2","order_2","pay", 3000L);
        OrderEvent ac3 = new OrderEvent("user_1","order_1","modify", 2000L);
        OrderEvent ac4 = new OrderEvent("user_1","order_1","pay", 60*60*1000L);

        DataStreamSource<OrderEvent> actions = env.fromElements(ac1, ac2,ac3, ac4, ac5);
        KeyedStream<OrderEvent, String> keyed =
                actions.assignTimestampsAndWatermarks(WatermarkStrategy.<OrderEvent>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                        .withTimestampAssigner(new SerializableTimestampAssigner<OrderEvent>() {
                            @Override
                            public long extractTimestamp(OrderEvent a, long l) {
                                return a.ts;
                            }
                        }))
                        .keyBy(value -> value.userId);

        Pattern<OrderEvent, OrderEvent> pattern = Pattern.<OrderEvent>begin("create")
                .where(new SimpleCondition<OrderEvent>() {
                    @Override
                    public boolean filter(OrderEvent userAction) throws Exception {
                        return userAction.eventType.equals("create");
                    }
                }).followedBy("pay")
                .where(new SimpleCondition<OrderEvent>() {
                    @Override
                    public boolean filter(OrderEvent userAction) throws Exception {
                        return userAction.eventType.equals("pay");
                    }
                }).within(Time.minutes(15));

        PatternStream<OrderEvent> patternStream = CEP.pattern(keyed, pattern);


        OutputTag<String> outputTag = new OutputTag<>("timeout") {};

        SingleOutputStreamOperator<String> stream = patternStream.process(new TimeoutProcess());

        stream.print();

        stream.getSideOutput(outputTag).print("timeout");

        env.execute();
    }

    public static class OrderEvent{
        public String userId;
        public String orderId;
        public String eventType;
        public long ts;

        public OrderEvent(String userId, String orderId, String eventType, long ts) {
            this.userId = userId;
            this.orderId = orderId;
            this.eventType = eventType;
            this.ts = ts;
        }
    }
    static class TimeoutProcess extends PatternProcessFunction<OrderEvent, String> implements TimedOutPartialMatchHandler<OrderEvent> {

        @Override
        public void processMatch(Map<String, List<OrderEvent>> map, Context context, Collector<String> collector) throws Exception {
            OrderEvent orderEvent = map.get("pay").get(0);
            collector.collect("" + orderEvent.userId + orderEvent.orderId + "pay");
        }

        @Override
        public void processTimedOutMatch(Map<String, List<OrderEvent>> map, Context context) throws Exception {
            OrderEvent orderEvent = map.get("create").get(0);
            OutputTag<String> outputTag = new OutputTag<>("timeout") {};
            context.output(outputTag, "" + orderEvent.userId + "timeout");
        }
    }
}
