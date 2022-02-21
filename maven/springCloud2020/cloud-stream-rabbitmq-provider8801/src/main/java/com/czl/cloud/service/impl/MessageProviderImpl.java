package com.czl.cloud.service.impl;

import com.czl.cloud.service.IMessageProvider;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.UUID;

@EnableBinding(Source.class)
public class MessageProviderImpl implements IMessageProvider {

    @Resource
    public MessageChannel output;

    @Override
    public void send() {
        output.send(MessageBuilder.withPayload(UUID.randomUUID().toString()).build());
    }
}
