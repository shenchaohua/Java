package com.czl.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @GetMapping("/testA")
    public String testA() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "testA";
    }

    @GetMapping("/testHotKey")
    @SentinelResource(value="/testHotKey",blockHandler="dealHandler")
    public String testHotKey(@RequestParam(required = false) String p1, @RequestParam(required = false) String p2) {
        return "testHotKey";
    }

    public String dealHandler(String p1, String p2, BlockException e) {
        return "now is blocked";
    }
}
