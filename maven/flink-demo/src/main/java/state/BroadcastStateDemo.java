package state;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.*;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BroadcastStateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Action> source1 = env.fromElements(
                new Action("czl", "login"),
                new Action("czl", "index"),
                new Action("czl1", "login"),
                new Action("czl1", "order")
        );

        DataStreamSource<Pattern> source2 = env.fromElements(
                new Pattern("login", "index"),
                new Pattern("login", "order")
        );
        MapStateDescriptor<Void, Pattern> bcStateDescriptor = new MapStateDescriptor<>("patterns", Types.VOID, Types.POJO(Pattern.class));
        BroadcastStream<Pattern> broadcastStream = source2.broadcast(bcStateDescriptor);
        source1.keyBy(one->one.userName)
                .connect(broadcastStream)
                .process(new KeyedBroadcastProcessFunction<String, Action, Pattern, Tuple2<String,Pattern>>() {
                    ValueState<String> lastAction;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        lastAction = getRuntimeContext().getState(new ValueStateDescriptor<String>("last", Types.STRING));
                    }

                    @Override
                    public void processElement(Action action, ReadOnlyContext readOnlyContext, Collector<Tuple2<String,Pattern>> collector) throws Exception {
                        ReadOnlyBroadcastState<Void, Pattern> broadcastState = readOnlyContext.getBroadcastState(new MapStateDescriptor<>("patterns", Types.VOID, Types.POJO(Pattern.class)));
                        Pattern pattern = broadcastState.get(null);
                        if (lastAction.value() != null && pattern != null) {
                            if (lastAction.value().equals(pattern.action1) && action.action.equals(pattern.action2)) {
                                collector.collect(Tuple2.of(action.userName, pattern));
                            }
                        }
                        lastAction.update(action.action);
                    }

                    @Override
                    public void processBroadcastElement(Pattern pattern, Context context, Collector<Tuple2<String,Pattern>> collector) throws Exception {
                        BroadcastState<Void, Pattern> broadcastState = context.getBroadcastState(new MapStateDescriptor<>("patterns", Types.VOID, Types.POJO(Pattern.class)));
                        // 流来数据后，覆盖
                        broadcastState.put(null, pattern);
                    }
                }).print();
        env.execute();
    }

    public static class Action {
        public String userName;
        public String action;

        public Action(String userName, String action) {
            this.userName = userName;
            this.action = action;
        }

        public Action() {
        }

        @Override
        public String toString() {
            return "Action{" +
                    "userName='" + userName + '\'' +
                    ", action='" + action + '\'' +
                    '}';
        }
    }
    public static class Pattern {
        public String action1;
        public String action2;

        public Pattern() {
        }

        public Pattern(String action1, String action2) {
            this.action1 = action1;
            this.action2 = action2;
        }

        @Override
        public String toString() {
            return "Pattern{" +
                    "action1='" + action1 + '\'' +
                    ", action2='" + action2 + '\'' +
                    '}';
        }
    }
}
