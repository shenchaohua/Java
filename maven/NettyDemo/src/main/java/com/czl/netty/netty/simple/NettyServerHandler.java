package com.czl.netty.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(ctx);
            Channel channel = ctx.channel();
            ChannelPipeline pipeline = ctx.pipeline();
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
            System.out.println(channel.remoteAddress());
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client2", CharsetUtil.UTF_8));
        });
        ctx.channel().eventLoop().schedule(()->{
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client3", CharsetUtil.UTF_8));
        }, 5, TimeUnit.SECONDS);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
