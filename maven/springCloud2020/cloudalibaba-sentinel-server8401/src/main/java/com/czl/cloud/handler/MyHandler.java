package com.czl.cloud.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Component;

public class MyHandler {

    public static String dealHandler(BlockException exception) {
        return "my handler, now is blocked, " + exception.getClass().getCanonicalName();
    }


}
