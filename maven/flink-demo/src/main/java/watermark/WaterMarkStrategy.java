package watermark;

import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.text.SimpleDateFormat;
import java.time.Duration;

public class WaterMarkStrategy {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStreamSource<String> data = env.socketTextStream("192.168.0.121", 7777);
        SingleOutputStreamOperator<Tuple2<String, Long>> maped = data.map(new MapFunction<String, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(String value) throws Exception {
                String[] split = value.split(",");
                return new Tuple2<>(split[0], Long.valueOf(split[1]));
            }
        });

        // 无延迟
        maped.assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple2<String, Long>>forMonotonousTimestamps()
            .withTimestampAssigner(new SerializableTimestampAssigner<Tuple2<String, Long>>() {
                @Override
                public long extractTimestamp(Tuple2<String, Long> s, long l) {
                    return s.f1;
                }
            })
        );

        // 有延迟
        maped.assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple2<String, Long>>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple2<String, Long>>() {
                    @Override
                    public long extractTimestamp(Tuple2<String, Long> stringLongTuple2, long l) {
                        return stringLongTuple2.f1;
                    }
                }
        ));

        maped.assignTimestampsAndWatermarks(new WatermarkStrategy<Tuple2<String, Long>>() {
            @Override
            public WatermarkGenerator<Tuple2<String, Long>> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
                return new WatermarkGenerator<Tuple2<String, Long>>() {
                    private Long delayTime=5000L;
                    private Long maxTs = -Long.MAX_VALUE + delayTime - 1L;
                    @Override
                    public void onEvent(Tuple2<String, Long> stringLongTuple2, long l, WatermarkOutput watermarkOutput) {
                        maxTs = Math.max(maxTs, stringLongTuple2.f1);
                    }

                    @Override
                    public void onPeriodicEmit(WatermarkOutput watermarkOutput) {
                        watermarkOutput.emitWatermark(new Watermark(maxTs - delayTime - 1L));
                    }
                };
            }

            @Override
            public TimestampAssigner<Tuple2<String, Long>> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
                return new TimestampAssigner<Tuple2<String, Long>>() {
                    @Override
                    public long extractTimestamp(Tuple2<String, Long> stringLongTuple2, long l) {
                        return stringLongTuple2.f1;
                    }
                };
            }
        });
    }
}
