package multiStream;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkGeneratorSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class BillCheckDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Tuple3<String,String, Long>> source1 = env.fromElements(
                new Tuple3("order-1", "app", 1000L),
                new Tuple3("order-2", "app", 3000L)
        );
        KeyedStream<Tuple3<String, String, Long>, String> stream1 = source1.assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, String, Long>>forBoundedOutOfOrderness(Duration.ZERO)
                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                    @Override
                    public long extractTimestamp(Tuple3<String, String, Long> stringStringLongTuple3, long l) {
                        return stringStringLongTuple3.f2;
                    }
                })).keyBy(one -> one.f0);

        DataStreamSource<Tuple3<String,String, Long>> source2 = env.fromElements(
                new Tuple3("order-1", "thirdPart", 4000L),
                new Tuple3("order-3", "thirdPart", 5000L)
        );
        KeyedStream<Tuple3<String, String, Long>, String> stream2 = source2.assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, String, Long>>forBoundedOutOfOrderness(Duration.ZERO)
                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                    @Override
                    public long extractTimestamp(Tuple3<String, String, Long> stringStringLongTuple3, long l) {
                        return stringStringLongTuple3.f2;
                    }
                })).keyBy(one -> one.f0);

        stream1.connect(stream2).process(new CoProcessFunction<Tuple3<String, String, Long>, Tuple3<String, String, Long>, String>() {
            private ValueState<Tuple3<String, String, Long>> appState;
            private ValueState<Tuple3<String, String, Long>> thirdState;

            @Override
            public void open(Configuration parameters) throws Exception {
                appState = getRuntimeContext().getState(
                        new ValueStateDescriptor<>("app-state", Types.TUPLE(Types.STRING, Types.STRING, Types.LONG)));
                thirdState = getRuntimeContext().getState(
                        new ValueStateDescriptor<>("third-state", Types.TUPLE(Types.STRING, Types.STRING, Types.LONG)));
            }

            @Override
            public void processElement1(Tuple3<String, String, Long> in, Context context, Collector<String> collector) throws Exception {
                if (thirdState.value() != null ) {

                    collector.collect("bill check" + thirdState.value() + in.toString());
                    thirdState.clear();
                }else {
                    appState.update(in);
                }
                context.timerService().registerEventTimeTimer(in.f2 + 5000);
            }

            @Override
            public void processElement2(Tuple3<String, String, Long> in, Context context, Collector<String> collector) throws Exception {
                if (appState.value() != null ) {

                    collector.collect("bill check" +  in.toString() + appState.value());
                    appState.clear();
                }else {
                    thirdState.update(in);
                }
                context.timerService().registerEventTimeTimer(in.f2);
            }

            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                if (appState.value() != null) {
                    out.collect("third part dismiss");
                }
                if (thirdState.value() != null) {
                    out.collect("app part dismiss");
                }
                appState.clear();
                thirdState.clear();
            }
        }).print();

        env.execute();
    }
}
