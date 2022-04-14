package table;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.time.Duration;

import static org.apache.flink.table.api.Expressions.$;

public class WindowTopNDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        tableEnv.executeSql("create table input (" +
                "username STRING, " +
                "url STRING, " +
                "ts BIGINT," +
                "et as TO_TIMESTAMP( FROM_UNIXTIME(ts/1000)), " +
                "WATERMARK FOR et as et - INTERVAL '1' SECOND" +
                ") with(" +
                "'connector'='filesystem', " +
                "'path'= 'input/sample.txt', " +
                "'format' = 'csv' " +
                ")");


        SingleOutputStreamOperator<TableApiDemo.Event> stream = env.fromElements(
                new TableApiDemo.Event("czl", 1),
                new TableApiDemo.Event("czl", 2)
        ).assignTimestampsAndWatermarks(WatermarkStrategy.<TableApiDemo.Event>forBoundedOutOfOrderness(Duration.ZERO)
                .withTimestampAssigner(new SerializableTimestampAssigner<TableApiDemo.Event>() {
                    @Override
                    public long extractTimestamp(TableApiDemo.Event e, long l) {
                        return e.ts;
                    }
                }));

        Table table = tableEnv.fromDataStream(stream, $("username"), $("ts"), $("et").rowtime());

        String subSql = "select " +
                "username, count(1) as cnt, window_start,window_end " +
                "from TABLE(TUMBLE(TABLE input, DESCRIPTOR(et), INTERVAL '1' second)) " +
                "group by username, window_start, window_end ";


        Table aggResult = tableEnv.sqlQuery("select " +
                "username " +
                "from (select username, row_number() over(partition by window_start,window_end order by cnt desc) as rn from " +
                "( " + subSql + " ) " +
                " )where rn<=2");
        tableEnv.toDataStream(aggResult).print();

        env.execute();
    }
}
