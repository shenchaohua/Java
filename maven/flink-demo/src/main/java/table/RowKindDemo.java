package table;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.types.RowKind;

import java.time.Duration;

import static org.apache.flink.table.api.Expressions.$;

public class RowKindDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Row> streamSource = env.fromElements(
                Row.ofKind(RowKind.INSERT, "czl", 1),
                Row.ofKind(RowKind.INSERT, "zf", 1),
                Row.ofKind(RowKind.UPDATE_BEFORE, "zf", 1),
                Row.ofKind(RowKind.UPDATE_AFTER, "zf", 2)
        );
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        Table table = tableEnv.fromChangelogStream(streamSource);

        env.execute();
    }

    public static class Event{
        public String username;
        public int ts;

        public Event(String username, int ts) {
            this.username = username;
            this.ts = ts;
        }

        public Event() {
        }
    }
}
