package com.czl.netty.netty.rpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Client {

    private static ExecutorService executor = Executors.newFixedThreadPool(1);

    private static ClientHandler clientHandler;

    private static NioEventLoopGroup eventExecutors;

    public Client () {

    }

    // 通过代理生成接口的实例，方法具体逻辑如下编写
    public Object getBean(final Class<?> serviceClass, final String providerName) {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[]{serviceClass}, (proxy, method, args) -> {
            if (clientHandler == null) {
                run();
            }
            clientHandler.setParam(providerName + args[0]);
            Object result = executor.submit(clientHandler).get();
            executor.shutdown();
            close();
            return result;
        });
    }

    public static void run() throws InterruptedException {
        clientHandler = new ClientHandler();

        eventExecutors = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventExecutors)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("decoder", new StringDecoder());
                        pipeline.addLast("encoder", new StringEncoder());
                        pipeline.addLast(clientHandler);
                    }
                });
        bootstrap.connect("127.0.0.1", 7000).sync();

    }

    public static void close() {
        eventExecutors.shutdownGracefully();
    }



}
