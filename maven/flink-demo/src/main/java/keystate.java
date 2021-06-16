import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class keystate {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Tuple2<Long, Long>> data = env.fromElements(Tuple2.of(1l, 3l), Tuple2.of(1l, 5l), Tuple2.of(1l, 7l),Tuple2.of(1l, 4l), Tuple2.of(1l, 2l), Tuple2.of(1l, 7l),Tuple2.of(1l, 4l), Tuple2.of(1l, 2l));
        KeyedStream<Tuple2<Long, Long>, Long> keyed = data.keyBy(value ->  value.f0);

        SingleOutputStreamOperator<Tuple2<Long, Long>> flatMaped =  keyed.flatMap(new RichFlatMapFunction<Tuple2<Long, Long>, Tuple2<Long, Long>>()
                {
                    private transient ValueState<Tuple2<Long, Long>> sum;
                    @Override
                    public void flatMap(Tuple2<Long, Long> value, Collector<Tuple2<Long,Long>> out) throws Exception {
                        Tuple2<Long, Long> currentSum = sum.value();
                        currentSum.f0 += 1;
                        currentSum.f1 += value.f1;
                        sum.update(currentSum);
                        if(currentSum.f0 >= 5) {
                            out.collect(new Tuple2<>(value.f0,currentSum.f1 / currentSum.f0));
                            sum.clear();
                        }
                    }
                    @Override
                    public void open(Configuration parameters) throws Exception {
                        ValueStateDescriptor<Tuple2<Long, Long>> descriptor = new ValueStateDescriptor<>(
                                "average",
                                TypeInformation.of(new TypeHint<Tuple2<Long, Long>>(){}),
                                Tuple2.of(0L, 0L)
                        );
                        sum = getRuntimeContext().getState(descriptor);
                    }
                });

        flatMaped.print();
        env.execute();
    }
}
