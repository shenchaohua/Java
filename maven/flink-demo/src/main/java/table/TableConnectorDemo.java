package table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class TableConnectorDemo {
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
                "url STRING, " +
                "ts BIGINT" +
                ") with(" +
                "'connector'='filesystem', " +
                "'path'= 'output/sample.txt', " +
                "'format' = 'csv' " +
                ")");
        Table tableResult = tableEnv.sqlQuery("select * from input where username='czl'");
        tableEnv.createTemporaryView("ret", tableResult);
        tableEnv.executeSql("insert into output select * from ret");
    }
}
