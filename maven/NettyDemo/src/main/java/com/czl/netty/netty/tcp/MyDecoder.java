package com.czl.netty.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        System.out.println("decoding");
        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);

        MyProtocol myProtocol = new MyProtocol();
        myProtocol.setLen(len);
        myProtocol.setContent(bytes);

        list.add(myProtocol);
    }
}
