package com.czl.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Sink.class)
public class MessageReceiveController {

    @Value("${server.port}")
    public String port;

    @StreamListener(Sink.INPUT)
    public void input(Message<String> message) {
        System.out.println(port + " " +  message.getPayload());
    }
}
