package cep;

import cep.MyPattern;
import cep.UserAction;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.*;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;


public class broadcast {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);
        UserAction ac1 = new UserAction(1001l, "login", 1000L);
        UserAction ac2 = new UserAction(1001l, "pay", 3000L);
        UserAction ac4 = new UserAction(1001l, "logout", 2000L);
        UserAction ac5 = new UserAction(1002l, "login", 1000L);
        UserAction ac6 = new UserAction(1002l, "logout", 2000L);
        UserAction ac3 = new UserAction(1002l, "car", 3000L);
        DataStreamSource<UserAction> actions = env.fromElements(ac1, ac2, ac3,ac4, ac5, ac6);
        MyPattern myPattern1 = new MyPattern("login", "logout");
//        MyPattern myPattern1 = new MyPattern("car", "logout");
        DataStreamSource<MyPattern> patterns = env.fromElements(myPattern1);
        KeyedStream<UserAction, Long> keyed =
                actions.assignTimestampsAndWatermarks(WatermarkStrategy.<UserAction>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                        .withTimestampAssigner(new SerializableTimestampAssigner<UserAction>() {
                            @Override
                            public long extractTimestamp(UserAction userAction, long l) {
                                return userAction.getTs();
                            }
                        }))
                .keyBy(value -> value.getUserId());
        //将模式流广播到下游的所有算子
        MapStateDescriptor<Void, MyPattern> bcStateDescriptor = new
                MapStateDescriptor<>("patterns", Types.VOID, Types.POJO(MyPattern.class));
        BroadcastStream<MyPattern> broadcastPatterns = patterns.broadcast(bcStateDescriptor);
        SingleOutputStreamOperator<Tuple2<Long, MyPattern>> process =
                keyed.connect(broadcastPatterns).process(new PatternEvaluator());
//将匹配成功的结果输出到控制台
        process.print();
        env.execute();
    }
    public static class PatternEvaluator extends KeyedBroadcastProcessFunction<Long,UserAction,MyPattern, Tuple2<Long,MyPattern>>
    {
        ValueState<String> prevActionState;
        @Override
        public void open(Configuration parameters) throws Exception {
            //初始化KeyedState
            prevActionState = getRuntimeContext().getState(new ValueStateDescriptor<String>("lastAction", Types.STRING));
        }
        //没来一个Action数据，触发一次执行
        @Override
        public void processElement(UserAction value, ReadOnlyContext ctx, Collector<Tuple2<Long, MyPattern>> out) throws Exception {
        //把用户行为流和模式流中的模式进行匹配
            ReadOnlyBroadcastState<Void, MyPattern> patterns = ctx.getBroadcastState(new MapStateDescriptor<>("patterns", Types.VOID, Types.POJO(MyPattern.class)));
            MyPattern myPattern = patterns.get(null);
            String prevAction = prevActionState.value();
            if(myPattern != null && prevAction != null) {
                if (myPattern.getFirstAction().equals(prevAction) && myPattern.getSecondAction().equals(value.getUserAction())) {
                    //如果匹配成...
                    out.collect(new Tuple2<>(ctx.getCurrentKey(),myPattern));
                } else {
                    //如果匹配不成功...
                }
            }
            prevActionState.update(value.getUserAction());
        }
        //每次来一个模式Pattern的时候触发执行
        @Override
        public void processBroadcastElement(MyPattern value, Context ctx, Collector<Tuple2<Long, MyPattern>> out) throws Exception {
            BroadcastState<Void, MyPattern> bcstate = ctx.getBroadcastState(new MapStateDescriptor<>("patterns", Types.VOID, Types.POJO(MyPattern.class)));
            bcstate.put(null,value);
        }
    }
}
