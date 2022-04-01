package com.czl.netty.netty.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Callable;

public class ClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private String result;

    private ChannelHandlerContext context;

    private String param;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext channelHandlerContext, Object s) throws Exception {
        result = s.toString();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public synchronized Object call() throws Exception {
        // 该方法负责与server通信
        context.writeAndFlush(param);
        wait();
        return result;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
