package state;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class FakeWindowDemo {
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

        stream1.keyBy(one->one.f0)
                .process(new WindowFunc(2L))
                .print();
        
        env.execute();
    }

    static class WindowFunc extends KeyedProcessFunction<String, Tuple3<String, String, Long>, String> {
        Long windowSize;
        // 为每个窗口保存count
        MapState<Long, Long> mapState;

        public WindowFunc(Long windowSize) {
            this.windowSize = windowSize;
        }

        @Override
        public void processElement(Tuple3<String, String, Long> in, Context context, Collector<String> collector) throws Exception {
            Long windowStart = in.f2 / windowSize * windowSize;
            Long windowEnd = windowStart + windowSize;

            context.timerService().registerEventTimeTimer(windowEnd - 1 );
            mapState.put(windowStart, mapState.contains(windowStart)? 1L : mapState.get(windowStart) + 1);
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            long windowEnd = timestamp + 1;
            long windowStart = windowEnd - windowSize;
            Long count = mapState.get(windowStart);
            out.collect(ctx.getCurrentKey() + windowStart + ":" + windowEnd + "count is" + count);

            mapState.remove(windowStart);
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            this.mapState = getRuntimeContext().getMapState(new MapStateDescriptor<Long, Long>("map", Types.LONG, Types.LONG));
        }
    }
}
