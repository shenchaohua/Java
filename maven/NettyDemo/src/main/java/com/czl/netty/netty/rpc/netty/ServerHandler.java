package com.czl.netty.netty.rpc.netty;

import com.czl.netty.netty.rpc.common.HelloService;
import com.czl.netty.netty.rpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object s) throws Exception {
        HelloService helloService = new HelloServiceImpl();
        if (s.toString().startsWith("HelloService#hello#")) {
            String resp = helloService.hello(s.toString());
            ctx.writeAndFlush(resp);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线");
    }
}
