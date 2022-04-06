package sink;

import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;

import java.util.concurrent.TimeUnit;

public class FileSink {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        DataStreamSource<Integer> source = env.fromElements(1, 2, 3);

        StreamingFileSink<Integer> streamingFileSink = StreamingFileSink
                .<Integer>forRowFormat(new Path("./output"), new SimpleStringEncoder<>("UTF-8"))
                .withRollingPolicy(DefaultRollingPolicy.builder()
                        .withMaxPartSize(10L)
                        .withRolloverInterval(TimeUnit.SECONDS.toSeconds(60L))
                        .withInactivityInterval(TimeUnit.SECONDS.toSeconds(30L))
                        .build())
                .build();


        source.addSink(streamingFileSink);
        env.execute();

    }
}
