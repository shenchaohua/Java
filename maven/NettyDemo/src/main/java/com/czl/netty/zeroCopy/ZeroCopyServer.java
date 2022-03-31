package com.czl.netty.zeroCopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ZeroCopyServer {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(8888));

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true) {
            int read = socketChannel.read(byteBuffer);
            if (read <= 0) {
                break;
            }
            byteBuffer.rewind();
        }



    }
}
