package com.lagou.task15;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class DataInputStreamTest {
    public static void main(String[] args) throws IOException {
        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(new FileInputStream("./a.txt"));
            int i = dataInputStream.readInt();
            System.out.println(i);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dataInputStream.close();
        }
    }
}
