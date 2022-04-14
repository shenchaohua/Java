package table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class TableConnectorPrintDemo {
    public static void main(String[] args) throws Exception {
//        EnvironmentSettings settings = EnvironmentSettings.newInstance()
//                .inStreamingMode()
//                .useBlinkPlanner()
//                .build();
//        TableEnvironment tableEnv = TableEnvironment.create(settings);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        tableEnv.executeSql("create table input (" +
                "username STRING, " +
                "url STRING, " +
                "ts BIGINT" +
                ") with(" +
                "'connector'='filesystem', " +
                "'path'= 'input/sample.txt', " +
                "'format' = 'csv' " +
                ")");
        tableEnv.executeSql("create table output (" +
                "username STRING, " +
                "cnt BIGINT" +
                ") with(" +
                "'connector'='print'" +
                ")");
        Table tableResult = tableEnv.sqlQuery("select username,count(url) as cnt from input where username='czl' group by username");
        tableEnv.createTemporaryView("ret", tableResult);
        tableEnv.executeSql("insert into output select * from ret");
    }
}
