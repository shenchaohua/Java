package source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ParallelSourceFunctionDemo {
    public static void main(String[] args) throws Exception {


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> source = env.addSource(new ParallelSourceFunction<String>() {
            private boolean flag = true;

            @Override
            public void run(SourceContext<String> sourceContext) throws Exception {
                Random random = new Random();

                String[] outs = new String[]{"hello", "world", "czl"};

                while (flag) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    sourceContext.collect(outs[random.nextInt(outs.length)]);
                }
            }

            @Override
            public void cancel() {
                flag = false;
            }
        }).setParallelism(2);

        source.map( one -> {
            System.out.println(one);
            return one + "---";
        }).print();

        env.execute();

    }
}
