package com.czl.netty.zeroCopy;

import java.io.*;
import java.net.Socket;

public class OldIoClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 7001);

        FileInputStream fileInputStream = new FileInputStream("./test.tar");

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] bytes = new byte[4096];
        int total = 0;
        long startTime = System.currentTimeMillis();
        while (true) {
            int read = fileInputStream.read(bytes, 0, bytes.length);
            if (read < 0) {
                break;
            }
            total += read;
            dataOutputStream.write(bytes);
        }
        System.out.println("传输" + total + "耗时" + (System.currentTimeMillis() - startTime));
        dataOutputStream.close();
        fileInputStream.close();
        socket.close();
    }
}
