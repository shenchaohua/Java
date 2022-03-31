package com.czl.netty.groupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

public class Server {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public Server() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(6666));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            while (true) {
                int select = selector.select();

                if (select >0 ) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    for (SelectionKey selectionKey : selectionKeys) {
                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + "上线");
                        }else if (selectionKey.isReadable()) {
                            readData(selectionKey);
                        }
                        selectionKeys.remove(selectionKey);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readData(SelectionKey selectionKey) {
        SocketChannel channel = (SocketChannel)selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true) {
            try {
                int read = channel.read(byteBuffer);
                if (read <= 0) {
                    break;
                }
                String msg = new String(byteBuffer.array(), 0, byteBuffer.position());
                System.out.println(msg);
                // 转发msg
                transferMsg(msg, channel);
                byteBuffer.clear();
            } catch (IOException e) {
                try {
                    System.out.println(channel.getRemoteAddress() + "下线");
                    selectionKey.cancel();
                    channel.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;
            }
        }
    }

    public void transferMsg(String msg, SocketChannel socketChannel) {
        for (SelectionKey selectedKey : selector.selectedKeys()) {
            if (selectedKey.channel() == socketChannel) {
                continue;
            }
            SocketChannel channel = (SocketChannel)selectedKey.channel();
            try {
                channel.write(ByteBuffer.wrap(msg.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.listen();
    }
}
