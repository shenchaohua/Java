package multiStream;

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

public class sideOutputDemo {
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

        OutputTag<Tuple3<String, Long, Long>> selfTag = new OutputTag<Tuple3<String, Long, Long>>("self") {};
        OutputTag<Tuple3<String, Long, Long>> themTag = new OutputTag<Tuple3<String, Long, Long>>("them") {};

        maped.process(new ProcessFunction<Tuple2<String, Long>, Tuple2<String, Long>>() {

            @Override
            public void processElement(Tuple2<String, Long> in, Context context, Collector<Tuple2<String, Long>> collector) throws Exception {
                if (in.f0.equals("self")){
                    context.output(selfTag, new Tuple3<>(in.f0, in.f1, 1L));
                } else if (in.f0.equals("them")) {
                    context.output(themTag, new Tuple3<>(in.f0, in.f1, 1L));
                }
                    collector.collect(in);
            }
        });
    }
}
