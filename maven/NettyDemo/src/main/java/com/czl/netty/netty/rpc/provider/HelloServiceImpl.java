package com.czl.netty.netty.rpc.provider;

import com.czl.netty.netty.rpc.common.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String msg) {
        return msg + "hello";
    }
}
