package com.czl.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

public class SelectorServerDemo {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int select = selector.select(1000);
            if (select == 0) {
                System.out.println("waiting for connect!");
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selectionKey : selectionKeys) {
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    ByteBuffer buffer = (ByteBuffer)selectionKey.attachment();
                    socketChannel.read(buffer);
                    System.out.println(new String(buffer.array(), 0, buffer.position()));
                }
                selectionKeys.remove(selectionKey);
            }
        }
    }
}
