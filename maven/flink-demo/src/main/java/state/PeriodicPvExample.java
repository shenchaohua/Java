package state;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class PeriodicPvExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Tuple3<String,String, Long>> source1 = env.fromElements(
                new Tuple3("order-1", "app", 1000L),
                new Tuple3("order-2", "app", 3000L)
        );
        SingleOutputStreamOperator<Tuple3<String, String, Long>> stream = source1.assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, String, Long>>forBoundedOutOfOrderness(Duration.ZERO)
                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                    @Override
                    public long extractTimestamp(Tuple3<String, String, Long> stringStringLongTuple3, long l) {
                        return stringStringLongTuple3.f2;
                    }
                }));
        stream.keyBy(one -> one.f0)
                .process(new KeyedProcessFunction<String, Tuple3<String, String, Long>, String>() {
                    ValueState<Long> pv;
                    ValueState<Long> timerTs;
                    @Override
                    public void processElement(Tuple3<String, String, Long> in, Context context, Collector<String> collector) throws Exception {
                        pv.update(pv.value()==null ? 1 : pv.value() + 1);

                        // 注册定时器
                        if (timerTs.value() == null) {
                            context.timerService().registerEventTimeTimer(in.f2 + 1000);
                            timerTs.update(in.f2 + 1000);
                        }
                    }

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        pv = getIterationRuntimeContext().getState(new ValueStateDescriptor<Long>("user-pv", Types.LONG));
                        timerTs = getIterationRuntimeContext().getState(new ValueStateDescriptor<Long>("timerTs", Types.LONG));
                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        out.collect(timestamp + ctx.getCurrentKey() + "pv: " + pv);
//                        pv.clear();
//                        ctx.timerService().registerEventTimeTimer(timestamp + 1000);
                        timerTs.clear();
                    }
                }).print();


        env.execute();
    }
}
