package com.czl.netty.zeroCopy;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class OldIoServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7001);
        while(true) {

            Socket socket = serverSocket.accept();

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            byte[] bytes = new byte[4096];
            while (true) {
                int read = dataInputStream.read(bytes);

                if (read == -1) {
                    break;
                }
            }


        }

    }
}
