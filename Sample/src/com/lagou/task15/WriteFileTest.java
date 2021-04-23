package com.lagou.task15;

import java.io.*;

public class WriteFileTest {
    public static void main(String[] args) {
        BufferedInputStream fileInputStream = null;
        BufferedOutputStream fileOutputStream = null;
        try {
            fileInputStream = new BufferedInputStream(new FileInputStream("F:\\Downloads\\[88k.me]ssni-536.mp4"));
            fileOutputStream = new BufferedOutputStream(new FileOutputStream("F:\\Downloads\\ssni-536.mp4")) ;

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            int res = 0;
            System.out.println("copy");
            while((res=fileInputStream.read())!=-1){
                System.out.println(res);
                fileOutputStream.write(res);
            }
            System.out.println("done");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        try {
            fileOutputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
