package com.czl.netty.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i=0;i<10;i++) {
//            ByteBuf byteBuf = Unpooled.copiedBuffer("hello,server" + i, CharsetUtil.UTF_8);
//            ctx.writeAndFlush(byteBuf);
            String msg = "hello,world!";
            byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);
            MyProtocol myProtocol = new MyProtocol();
            myProtocol.setLen(msgBytes.length);
            myProtocol.setContent(msgBytes);
            ctx.writeAndFlush(myProtocol);
        }
    }
}
