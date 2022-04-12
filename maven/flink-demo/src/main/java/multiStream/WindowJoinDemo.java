package multiStream;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class WindowJoinDemo {
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
                new Tuple3("order-1", "thirdPart", 4000L),
                new Tuple3("order-3", "thirdPart", 5000L)
        );
        SingleOutputStreamOperator<Tuple3<String, String, Long>> stream2 = source2.assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, String, Long>>forBoundedOutOfOrderness(Duration.ZERO)
                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                    @Override
                    public long extractTimestamp(Tuple3<String, String, Long> stringStringLongTuple3, long l) {
                        return stringStringLongTuple3.f2;
                    }
                }));

        stream1.join(stream2)
                .where(one->one.f0)
                .equalTo(one->one.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(2)))
                .apply(new JoinFunction<Tuple3<String, String, Long>, Tuple3<String, String, Long>, String>() {
                    @Override
                    public String join(Tuple3<String, String, Long> s1, Tuple3<String, String, Long> s2) throws Exception {
                        return s1 + s2.toString();
                    }
                }).print();

        env.execute();
    }
}
