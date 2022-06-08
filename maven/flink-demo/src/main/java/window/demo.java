package window;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.windows.Window;

import java.text.SimpleDateFormat;
import java.util.Collection;

public class demo {
    public static void main(String[] args) throws Exception {
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

        KeyedStream<Tuple2<String, Long>, String> keyedStream = maped.keyBy(one -> one.f0);

//        keyedStream.countWindow()

        keyedStream.window(new WindowAssigner<Tuple2<String, Long>, Window>() {
            @Override
            public Collection<Window> assignWindows(Tuple2<String, Long> stringLongTuple2, long l, WindowAssignerContext windowAssignerContext) {
                return null;
            }

            @Override
            public Trigger<Tuple2<String, Long>, Window> getDefaultTrigger(StreamExecutionEnvironment streamExecutionEnvironment) {
                return null;
            }

            @Override
            public TypeSerializer<Window> getWindowSerializer(ExecutionConfig executionConfig) {
                return null;
            }

            @Override
            public boolean isEventTime() {
                return false;
            }
        }).aggregate(new AggregateFunction<Tuple2<String, Long>, Tuple2<Long, Integer>, Long>() {

            @Override
            public Tuple2<Long, Integer> createAccumulator() {
                return Tuple2.of(0L,0);
            }

            // 累加器的增量计算
            @Override
            public Tuple2<Long, Integer> add(Tuple2<String, Long> e, Tuple2<Long, Integer> acc) {
                acc.f1 ++;
                acc.f0 += e.f1;
                return acc;
            }

            @Override
            public Long  getResult(Tuple2<Long, Integer> acc) {
                return acc.f0/ acc.f1;
            }

            @Override
            public Tuple2<Long, Integer> merge(Tuple2<Long, Integer> acc1, Tuple2<Long, Integer> acc2) {
                return Tuple2.of(acc1.f0 + acc2.f0, acc1.f1 + acc2.f1);
            }
        });

        env.execute();
    }
}
