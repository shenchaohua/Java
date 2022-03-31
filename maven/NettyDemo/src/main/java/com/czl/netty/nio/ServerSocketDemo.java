package com.czl.netty.nio;

import javafx.scene.media.SubtitleTrack;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ServerSocketDemo {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress socketAddress = new InetSocketAddress(6666);

        serverSocketChannel.socket().bind(socketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];

        byteBuffers[0] = ByteBuffer.allocate(2);
        byteBuffers[1] = ByteBuffer.allocate(5);

        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true) {
            int byteRead = 0;
            while (byteRead < 7) {
                long read = socketChannel.read(byteBuffers);
                byteRead += read;
                Arrays.asList(byteBuffers).stream()
                        .map(byteBuffer -> byteBuffer.position() + " " + byteBuffer.limit())
                        .forEach(System.out::println);
            }

            Arrays.asList(byteBuffers).stream().forEach(byteBuffer -> byteBuffer.flip());

            int byteWrite = 0;

            while (byteWrite < 7) {
                long write = socketChannel.write(byteBuffers);
                byteWrite += write;
            }

            Arrays.asList(byteBuffers).stream().forEach(byteBuffer -> byteBuffer.clear());
        }
    }
}
