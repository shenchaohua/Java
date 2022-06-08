package udf;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.TableFunction;

public class TableFunctionDemo {
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

        tableEnv.createTemporaryFunction("split", MyFunction.class);

        Table table = tableEnv.sqlQuery("select username,s,len from input, lateral table(split(username)) as T(s,len)");

        tableEnv.toDataStream(table).print();

        env.execute();

    }

    public static class MyFunction extends TableFunction<Tuple2<String,Integer>> {

        public void eval(String field) {
            for (String s : field.split(" ")) {
                collect(Tuple2.of(s, s.length()));
            }
        }
    }
}
