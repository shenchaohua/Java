package udf;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.AggregateFunction;
import org.apache.flink.table.functions.ScalarFunction;
import scala.Int;

public class AggregateFunctionDemo {
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

        tableEnv.createTemporaryFunction("avg_weight", MyFunction.class);

        Table table = tableEnv.sqlQuery("select username,avg_weight(ts) from input group by username");

        tableEnv.toChangelogStream(table).print();

        env.execute();

    }

    public static class MyAccumulator {
        public Long sum;
        public int count;

        public MyAccumulator(Long sum, int count) {
            this.sum = sum;
            this.count = count;
        }
    }

    public static class MyFunction extends AggregateFunction<Long, MyAccumulator> {

        @Override
        public MyAccumulator createAccumulator() {
            return new MyAccumulator(0L,0);
        }

        @Override
        public Long getValue(MyAccumulator myAccumulator) {
            if (myAccumulator.count == 0) {
                return null;
            }
            return myAccumulator.sum / myAccumulator.count;
        }

        public void accumulate(MyAccumulator myAccumulator, Long ts) {
            myAccumulator.sum += ts;
            myAccumulator.count ++ ;
        }
    }
}
