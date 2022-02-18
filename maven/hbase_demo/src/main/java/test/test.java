package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class test {
    public static HashMap<String,String> billings = new HashMap<String,String>(){
        {
            billings.put("yingyongbao_quotation","sss");
        }
    };


    public static void main(String[] args) throws IOException {

        System.out.println(billings);
        Process proc = Runtime.getRuntime().exec("notepad.exe");
        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));
        System.out.println(stdError);
    }
}
