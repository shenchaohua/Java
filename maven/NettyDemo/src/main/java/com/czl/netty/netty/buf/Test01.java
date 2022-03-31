package com.czl.netty.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class Test01 {
    public static void main(String[] args) {

        ByteBuf buffer = Unpooled.buffer(10);

        for (int i=0;i<11;i++) {
            buffer.writeByte(i);
        }

        Unpooled.copiedBuffer("aaa", CharsetUtil.UTF_8);
    }
}
