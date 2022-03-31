package com.czl.netty.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler2 extends SimpleChannelInboundHandler<MyProtocol> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyProtocol msg) throws Exception {
        System.out.println(msg);
    }
}
