package multiStream;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

public class ConnectDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Integer> source = env.fromElements(1, 2, 3);
        DataStreamSource<Long> source1 = env.fromElements(4L, 5L);

        source.connect(source1).map(new CoMapFunction<Integer, Long, String>() {
            @Override
            public String map1(Integer integer) throws Exception {
                return "int " + integer;
            }

            @Override
            public String map2(Long aLong) throws Exception {
                return "Long " + aLong;
            }
        }).print();
    }
}
