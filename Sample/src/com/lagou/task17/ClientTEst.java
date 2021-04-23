package com.lagou.task17;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ClientTEst {
    public static void main(String[] args) throws IOException {
        Socket socket  = null;
        try {
            socket = new Socket("127.0.0.1",8888);
            PrintStream ps = new PrintStream(socket.getOutputStream());
            ps.println("hello");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
