package multiStream;

import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.text.SimpleDateFormat;
import java.time.Duration;

public class UnionDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStreamSource<String> stream1 = env.socketTextStream("192.168.0.121", 7777);
        DataStreamSource<String> stream2 = env.socketTextStream("192.168.0.121", 7776);

        SingleOutputStreamOperator<Tuple2<String, Long>> s1 = stream1.map(new MapFunction<String, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(String value) throws Exception {
                String[] split = value.split(",");
                return new Tuple2<>(split[0], Long.valueOf(split[1]));
            }
        }).assignTimestampsAndWatermarks(new WatermarkStrategy<Tuple2<String, Long>>() {
            @Override
            public WatermarkGenerator<Tuple2<String, Long>> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
                return new BoundedOutOfOrdernessWatermarks<>(Duration.ofSeconds(2));
            }
        }.withTimestampAssigner(new SerializableTimestampAssigner<Tuple2<String, Long>>() {
            @Override
            public long extractTimestamp(Tuple2<String, Long> in, long l) {
                return in.f1;
            }
        }));

        SingleOutputStreamOperator<Tuple2<String, Long>> s2 = stream2.map(new MapFunction<String, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(String value) throws Exception {
                String[] split = value.split(",");
                return new Tuple2<>(split[0], Long.valueOf(split[1]));
            }
        }).assignTimestampsAndWatermarks(new WatermarkStrategy<Tuple2<String, Long>>() {
            @Override
            public WatermarkGenerator<Tuple2<String, Long>> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
                return new BoundedOutOfOrdernessWatermarks<>(Duration.ofSeconds(5));
            }
        }.withTimestampAssigner(new SerializableTimestampAssigner<Tuple2<String, Long>>() {
            @Override
            public long extractTimestamp(Tuple2<String, Long> in, long l) {
                return in.f1;
            }
        }));

        s1.union(s2).process(new ProcessFunction<Tuple2<String, Long>, String>() {
            @Override
            public void processElement(Tuple2<String, Long> stringLongTuple2, Context context, Collector<String> collector) throws Exception {
                collector.collect(stringLongTuple2.f0 + context.timerService().currentWatermark());
            }
        }).print();
    }
}
