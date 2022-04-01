package com.czl.netty.netty.rpc.customer;

import com.czl.netty.netty.rpc.common.HelloService;
import com.czl.netty.netty.rpc.netty.Client;

public class ClientBoostrap {
    public static void main(String[] args) throws InterruptedException {

        Client client = new Client();

        HelloService helloService = (HelloService) client.getBean(HelloService.class, "HelloService#hello#");

        String result = helloService.hello("czl");

        System.out.println(result);

    }
}
