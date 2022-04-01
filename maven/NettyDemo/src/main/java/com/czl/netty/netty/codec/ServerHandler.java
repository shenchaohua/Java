package com.czl.netty.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class ServerHandler extends SimpleChannelInboundHandler<Long> {

    private EventExecutorGroup eventExecutors = new DefaultEventExecutorGroup(16);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
        System.out.println(aLong);
        channelHandlerContext.channel().writeAndFlush(1L);
    }
}
