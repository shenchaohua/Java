package com.czl.netty.groupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.Set;

public class Client {

    private SocketChannel socketChannel;

    private Selector selector;

    public Client() {
        try {
            socketChannel = SocketChannel.open();
            selector = Selector.open();

            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 6666));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
            while (! socketChannel.finishConnect()) {
                System.out.println("连接中");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (selector.isOpen()) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectedKey : selectionKeys) {
                    if (selectedKey.isReadable()) {
                        receiveMsg();
                    }
                    selectionKeys.remove(selectedKey);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(String msg) {
        try {
            msg = socketChannel.getLocalAddress().toString() + "say: " + msg;
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMsg() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            while (true) {
                int read = socketChannel.read(byteBuffer);
                if (read <= 0) {
                    break;
                }
                System.out.println(new String(byteBuffer.array(), 0, byteBuffer.position()));
                byteBuffer.clear();
            }

        } catch (IOException e) {
            System.out.println("服务已关闭");
            try {
                selector.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();

        new Thread(client::run).start();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            client.sendInfo(nextLine);
        }
    }
}
