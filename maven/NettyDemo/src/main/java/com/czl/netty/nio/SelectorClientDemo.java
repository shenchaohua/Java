package com.czl.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SelectorClientDemo {

    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        if (! socketChannel.connect(new InetSocketAddress("127.0.0.1",6666)) ) {

            while (!socketChannel.finishConnect()) {
                System.out.println("connecting, 可以做其他事情");
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap("hello".getBytes());
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
