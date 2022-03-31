package com.czl.netty.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyEncoder extends MessageToByteEncoder<MyProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MyProtocol myProtocol, ByteBuf byteBuf) throws Exception {
        System.out.println("encoding");
        byteBuf.writeInt(myProtocol.getLen());
        byteBuf.writeBytes(myProtocol.getContent());
    }
}
