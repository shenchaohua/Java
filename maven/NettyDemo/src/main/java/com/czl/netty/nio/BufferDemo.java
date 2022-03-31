package com.czl.netty.nio;

import java.nio.Buffer;
import java.nio.IntBuffer;

public class BufferDemo {

    public static void main(String[] args) {


        IntBuffer intBuffer = IntBuffer.allocate(5);
        intBuffer.put(1);

        // 读写切换
        Buffer flip = intBuffer.flip();
        while (flip.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
