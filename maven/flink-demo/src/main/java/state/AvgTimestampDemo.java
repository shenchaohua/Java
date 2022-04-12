package state;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.RichFlatJoinFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.*;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class AvgTimestampDemo {
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
                .flatMap(new AvgFlatFunc())
                .print();
        
        env.execute();
    }

    static class AvgFlatFunc extends RichFlatMapFunction<Tuple3<String, String, Long>, String> {
        AggregatingState<Tuple3<String, String, Long>, Long> aggregatingState;
        ValueState<Long> count;

        @Override
        public void flatMap(Tuple3<String, String, Long> in, Collector<String> collector) throws Exception {
            aggregatingState.add(in);
            count.update(count.value() == null? 1 : count.value() + 1);
            if (count.value() == 5) {
                count.clear();
                collector.collect(in.f0 + "" + count + "平均访问时间戳为" + aggregatingState.get());
                aggregatingState.clear();
            }
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            aggregatingState = getRuntimeContext().getAggregatingState(
                    new AggregatingStateDescriptor<Tuple3<String, String, Long>, Tuple2<Long,Long>, Long>("agg", new AggregateFunction<Tuple3<String, String, Long>, Tuple2<Long, Long>, Long>() {
                        @Override
                        public Tuple2<Long, Long> createAccumulator() {
                            return new Tuple2<Long, Long>(0L,0L);
                        }

                        @Override
                        public Tuple2<Long, Long> add(Tuple3<String, String, Long> in, Tuple2<Long, Long> acc) {
                            return Tuple2.of(acc.f0 + in.f2, acc.f1 + 1);
                        }

                        @Override
                        public Long getResult(Tuple2<Long, Long> acc) {
                            return acc.f0 / acc.f1;
                        }

                        @Override
                        public Tuple2<Long, Long> merge(Tuple2<Long, Long> acc, Tuple2<Long, Long> acc1) {
                            return Tuple2.of(acc.f0 + acc1.f0, acc.f1 + acc1.f1);
                        }
                    }, Types.TUPLE(Types.LONG, Types.LONG)));
            ValueStateDescriptor<Long> valueStateDescriptor = new ValueStateDescriptor<>("count", Types.LONG);
            this.count = getRuntimeContext().getState(valueStateDescriptor);
            StateTtlConfig ttlConfig = StateTtlConfig.newBuilder(Time.seconds(10))
                    .setUpdateType(StateTtlConfig.UpdateType.OnReadAndWrite)
                    .setStateVisibility(StateTtlConfig.StateVisibility.ReturnExpiredIfNotCleanedUp)
                    .build();
            valueStateDescriptor.enableTimeToLive(ttlConfig);
        }
    }

}
