package com.lagou.task15;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterTest {
    public static void main(String[] args) {
        FileWriter fw = null;
        FileReader fw2 = null;
        try {
//            fw = new FileWriter("./a.txt");
            fw2 = new FileReader("./a.txt");
//            fw.write('a');
//            fw.write(new char[]{'b','c'},1,1);
//            int i = fw2.read();
            char[] arr = new char[5];
            fw2.read(arr,1,2);
            System.out.println(arr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
//                fw.close();
                fw2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
