package udf;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.AggregateFunction;
import org.apache.flink.table.functions.TableAggregateFunction;
import org.apache.flink.util.Collector;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.call;

public class TableAggregateFunctionDemo {
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

        tableEnv.createTemporaryFunction("top2", Top2.class);
        String subSql = "select " +
                "username, count(1) as cnt, window_start,window_end " +
                "from TABLE(TUMBLE(TABLE input, DESCRIPTOR(et), INTERVAL '1' second)) " +
                "group by username, window_start, window_end ";
        Table subTable = tableEnv.sqlQuery(subSql);

        Table table = subTable.groupBy($("window_end")).flatAggregate(call("top2", $("cnt")).as("value", "rank"))
                .select($("window_end"), $("value"), $("rank"));

        tableEnv.toChangelogStream(table).print();

        env.execute();

    }
    public static class MyAccumulator {
        public Long first;
        public Long next;

    }

    public static class Top2 extends TableAggregateFunction<Tuple2<Long,Integer>, MyAccumulator> {

        @Override
        public MyAccumulator createAccumulator() {
            MyAccumulator accumulator = new MyAccumulator();
            accumulator.first = Long.MIN_VALUE;
            accumulator.next = Long.MIN_VALUE;
            return accumulator;
        }

        public void accumulate(MyAccumulator myAccumulator, Long ts) {
            if (ts > myAccumulator.first) {
                myAccumulator.next = myAccumulator.first;
                myAccumulator.first = ts;
            }else if (ts > myAccumulator.next) {
                myAccumulator.next = ts;
            }
        }

        public void emitValue(MyAccumulator accumulator, Collector<Tuple2<Long, Integer>> collector) {
            if (accumulator.first != Long.MIN_VALUE) {
                collector.collect(Tuple2.of(accumulator.first, 1));
            }
            if (accumulator.next != Long.MIN_VALUE) {
                collector.collect(Tuple2.of(accumulator.next, 2));
            }

        }
    }
}
