package source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SourceFunctionDemo {
    public static void main(String[] args) throws Exception {


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> source = env.addSource(new SourceFunction<String>() {
            private boolean flag = true;

            @Override
            public void run(SourceContext<String> sourceContext) throws Exception {
                Random random = new Random();

                String[] outs = new String[]{"hello", "world", "czl"};

                while (flag) {
                    TimeUnit.SECONDS.sleep(3);
                    sourceContext.collect(outs[random.nextInt(outs.length)]);
                }
            }

            @Override
            public void cancel() {
                flag = false;
            }
        });

        source.map( one -> {
            System.out.println(one);
            return one + "---";
        }).print();

        env.execute();

    }
}
