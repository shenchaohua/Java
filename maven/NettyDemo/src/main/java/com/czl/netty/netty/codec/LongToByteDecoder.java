package com.czl.netty.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class LongToByteDecoder extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Long o, ByteBuf byteBuf) throws Exception {
        System.out.println("long to byte");
        byteBuf.writeLong(o);
    }
}
