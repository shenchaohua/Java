package com.czl.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ClientController {

    @Value("${config.info}")
    public String config;

    @GetMapping("/config/info")
    public String get(){
        return config;
    }
}
