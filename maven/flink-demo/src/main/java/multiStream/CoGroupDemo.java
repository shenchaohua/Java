package multiStream;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class CoGroupDemo {
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

        stream1.coGroup(stream2)
                .where(one->one.f0)
                .equalTo(one->one.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(2)))
                .apply(new CoGroupFunction<Tuple3<String, String, Long>, Tuple3<String, String, Long>, String>() {
                    @Override
                    public void coGroup(Iterable<Tuple3<String, String, Long>> iterable, Iterable<Tuple3<String, String, Long>> iterable1, Collector<String> collector) throws Exception {
                        collector.collect(iterable + iterable1.toString());
                    }
                }).print();

        env.execute();
    }
}
