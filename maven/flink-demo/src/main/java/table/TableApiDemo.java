package table;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import scala.Int;

import java.time.Duration;

import static org.apache.flink.table.api.Expressions.$;

public class TableApiDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

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
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        Table table = tableEnv.fromDataStream(stream);
        Table table2 = tableEnv.fromDataStream(stream, $("username").as("user"), $("ts"));
//        Table table3 = tableEnv.fromChangelogStream(stream, $("username").as("user"), $("ts"));
        tableEnv.createTemporaryView("test", stream, $("username").as("user"), $("ts"));

        Table resultTab = tableEnv.sqlQuery("select username,ts from " + table);
        Table resultTab2 = table.select($("username"), $("ts")).where($("ts").isEqual(1));
        tableEnv.toDataStream(resultTab).print("1");
        DataStream<Row> rowDataStream = tableEnv.toChangelogStream(resultTab);
        tableEnv.toDataStream(resultTab2).print("1");
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
