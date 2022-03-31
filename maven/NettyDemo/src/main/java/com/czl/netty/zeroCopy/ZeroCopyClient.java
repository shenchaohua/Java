package com.czl.netty.zeroCopy;

import com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverLocalFilesystem;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class ZeroCopyClient {
    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));

        FileInputStream fileInputStream = new FileInputStream("./test.tar");

        FileChannel fileChannel = fileInputStream.getChannel();
        long startTime = System.currentTimeMillis();
        // window下需要分段传输，一次8m
        long transferTo = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println(transferTo + "耗时" + (System.currentTimeMillis()-startTime));
    }
}
