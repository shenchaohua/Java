package cep;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.functions.TimedOutPartialMatchHandler;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class NFADemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        UserAction ac1 = new UserAction(1001l, "fail", 1000L);
        UserAction ac2 = new UserAction(1001l, "fail", 3000L);
        UserAction ac3 = new UserAction(1001l, "fail", 2000L);
        UserAction ac4 = new UserAction(1002l, "fail", 1000L);
        UserAction ac5 = new UserAction(1002l, "fail", 2000L);

        KeyedStream<UserAction, Long> keyedStream = env.fromElements(ac1, ac2, ac3, ac4, ac5).keyBy(one -> one.getUserId());

        keyedStream.flatMap(new StateManagerFlatMap()).print();

        env.execute();
    }

    public static class StateManagerFlatMap extends RichFlatMapFunction<UserAction, String> {
        ValueState<State> currentState;

        @Override
        public void open(Configuration parameters) throws Exception {
            currentState = getRuntimeContext().getState(new ValueStateDescriptor<State>("state", State.class));
        }

        @Override
        public void flatMap(UserAction userAction, Collector<String> collector) throws Exception {
            State state = currentState.value();
            if (state == null ){
                state = State.Initial;
            }
            State nextState = state.transition(userAction.getUserAction());
            if (nextState == State.Match) {
                collector.collect("" + userAction.getUserId() + "match");
            } else if (nextState == State.Terminal) {
                currentState.update(State.Initial);
            } else {
                currentState.update(nextState);
            }
        }
    }

    public enum State {
        Terminal,
        Match,
        S2(new Transition("fail", Match), new Transition("success", Terminal)),
        S1(new Transition("fail", S2), new Transition("success", Terminal)),
        Initial(new Transition("fail", S1), new Transition("success", Terminal));

        private Transition[] transitions;
        State(Transition... transitions) {
            this.transitions = transitions;
        }
        public State transition(String eventType) {
            for (Transition transition : transitions) {
                if (transition.getEventType().equals(eventType)) {
                    return transition.getTargetState();
                }
            }
            return Initial;
        }

    }
    static class Transition {
        private String eventType;
        private State targetState;

        public Transition(String eventType, State targetState) {
            this.eventType = eventType;
            this.targetState = targetState;
        }

        public String getEventType() {
            return eventType;
        }

        public State getTargetState() {
            return targetState;
        }
    }
}
