package com.czl.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    public static void main(String[] args) throws IOException {

        ExecutorService pool = Executors.newCachedThreadPool();

        ServerSocket socket = new ServerSocket(6666);

        while (true) {
            System.out.println("等待连接中");
            Socket accept = socket.accept();
            System.out.println("conn arrived");

            pool.submit(() -> handlerConn(accept));
        }

    }

    public static void handlerConn(Socket socket) {
        byte[] bytes = new byte[1024];
        try {
            InputStream inputStream = socket.getInputStream();
            while (true) {
                System.out.println("等待数据中");
                int read = inputStream.read(bytes);
                if (read == 0) {
                    break;
                }
                System.out.println(Thread.currentThread().getName() + new String(bytes, 0, read));
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
