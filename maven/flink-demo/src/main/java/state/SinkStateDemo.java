package state;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SinkStateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Tuple3<String,String, Long>> source1 = env.fromElements(
                new Tuple3("order-1", "app", 1000L),
                new Tuple3("order-2", "app", 3000L)
        );
        SingleOutputStreamOperator<Tuple3<String, String, Long>> stream1 = source1.assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, String, Long>>forBoundedOutOfOrderness(Duration.ZERO)
                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                    @Override
                    public long extractTimestamp(Tuple3<String, String, Long> stringStringLongTuple3, long l) {
                        return stringStringLongTuple3.f2;
                    }
                }));

        stream1.addSink(new SelfSink());

        env.execute();
    }

    static class SelfSink implements SinkFunction<Tuple3<String, String, Long>>,CheckpointedFunction {
        ListState<Tuple3<String, String, Long>> checkPointedState;
        List<Tuple3<String, String, Long>> bufferList;

        public SelfSink() {
            this.bufferList = new ArrayList<>();
        }

        @Override
        public void snapshotState(FunctionSnapshotContext functionSnapshotContext) throws Exception {
            checkPointedState.clear();
            for (Tuple3<String, String, Long> one : bufferList) {
                checkPointedState.add(one);
            }
        }

        @Override
        public void initializeState(FunctionInitializationContext functionInitializationContext) throws Exception {
            ListStateDescriptor<Tuple3<String, String, Long>> listStateDescriptor =
                    new ListStateDescriptor<>("list", Types.TUPLE(Types.STRING, Types.STRING, Types.LONG));
            checkPointedState = functionInitializationContext.getOperatorStateStore().getListState(listStateDescriptor);
            if (functionInitializationContext.isRestored()) {
                for (Tuple3<String, String, Long> one : checkPointedState.get()) {
                    bufferList.add(one);
                }
            }
        }

        @Override
        public void invoke(Tuple3<String, String, Long> value, Context context) throws Exception {
            bufferList.add(value);
            if (bufferList.size() == 10) {
                for (Tuple3<String, String, Long> one : bufferList) {
                    System.out.println(one);
                }
                bufferList.clear();
            }
        }
    }
}
