package com.lagou.task17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {
    public static void main(String[] args) {
        ServerSocket s = null;
        try {
            s = new ServerSocket(8888);
            Socket accept = s.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(accept.getInputStream()));
            String s1 = br.readLine();
            System.out.println(s1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
