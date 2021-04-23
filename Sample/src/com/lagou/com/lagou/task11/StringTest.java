package com.lagou.com.lagou.task11;

public class StringTest {
    public static void main(String[] args) {
        String str = new String("1234");
        System.out.println(str);
        int ib=0;
        for(int i=0;i<str.length();i++){
            ib = ib*10+str.charAt(i)-'0';
        }
    }



}
