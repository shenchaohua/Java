package com.czl.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.czl.cloud.handler.MyHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlowLimitController {

    @GetMapping("/getResource")
    @SentinelResource(value = "getResource", blockHandler = "dealHandler", blockHandlerClass = MyHandler.class)
    public String getResource(){
        return "get resource";
    }

    public String dealHandler(BlockException exception) {
        return "now is blocked, " + exception.getClass().getCanonicalName();
    }
}
