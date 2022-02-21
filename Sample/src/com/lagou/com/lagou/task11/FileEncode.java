package com.lagou.com.lagou.task11;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileEncode {
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/kelvin/Downloads/search_20210711.txt");
        InputStream is = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(is,"gbk");
        BufferedReader br = new BufferedReader(isr);
        String str = "";
        while (null != (str = br.readLine())) {
            System.out.println(str);
        }
        br.close();
    }
}
