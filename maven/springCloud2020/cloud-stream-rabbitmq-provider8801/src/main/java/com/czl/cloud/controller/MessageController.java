package com.czl.cloud.controller;

import com.czl.cloud.service.IMessageProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MessageController {

    @Resource
    public IMessageProvider iMessageProvider;

    @GetMapping("/message/send")
    public void send() {
        iMessageProvider.send();
    }
}
