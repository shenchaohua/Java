package state;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
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

public class FullJoinDemo {
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
        DataStreamSource<Tuple3<String,String, Long>> source2 = env.fromElements(
                new Tuple3("order-1", "third", 1000L),
                new Tuple3("order-3", "third", 3000L)
        );
        SingleOutputStreamOperator<Tuple3<String, String, Long>> stream2 = source2.assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, String, Long>>forBoundedOutOfOrderness(Duration.ZERO)
                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                    @Override
                    public long extractTimestamp(Tuple3<String, String, Long> stringStringLongTuple3, long l) {
                        return stringStringLongTuple3.f2;
                    }
                }));
        stream1.keyBy(one->one.f0)
                .connect(stream2.keyBy(one->one.f0))
                .process(new CoProcessFunction<Tuple3<String, String, Long>, Tuple3<String, String, Long>, String>() {
                    ListState<Tuple3<String, String, Long>> stream1List;
                    ListState<Tuple3<String, String, Long>> stream2List;
                    @Override
                    public void processElement1(Tuple3<String, String, Long> in, Context context, Collector<String> collector) throws Exception {
                        for (Tuple3<String, String, Long> right : stream2List.get()) {
                            collector.collect(in + "->" + right);
                        }
                        stream1List.add(in);
                    }

                    @Override
                    public void processElement2(Tuple3<String, String, Long> in, Context context, Collector<String> collector) throws Exception {
                        for (Tuple3<String, String, Long> right : stream1List.get()) {
                            collector.collect(in + "->" + right);
                        }
                        stream2List.add(in);
                    }

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        stream1List = getRuntimeContext().getListState(new ListStateDescriptor<Tuple3<String, String, Long>>("list1", Types.TUPLE(Types.STRING,Types.STRING,Types.LONG)));
                        stream2List = getRuntimeContext().getListState(new ListStateDescriptor<Tuple3<String, String, Long>>("list1", Types.TUPLE(Types.STRING,Types.STRING,Types.LONG)));
                    }
                }).print();


        env.execute();
    }
}
