package table;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.time.Duration;

import static org.apache.flink.table.api.Expressions.$;

public class TableApiDemo2 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        SingleOutputStreamOperator<Event> stream = env.fromElements(
                new Event("czl", 1),
                new Event("czl", 2)
        ).assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event e, long l) {
                        return e.ts;
                    }
                }));

        tableEnv.executeSql("create temporary table xxx ... with ('connector'=....) ");
        TableResult tableResult = tableEnv.executeSql("select * from xxx");
        Table tableResult2 = tableEnv.from("xxx").select();
        tableResult2.executeInsert("outputTable");
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
