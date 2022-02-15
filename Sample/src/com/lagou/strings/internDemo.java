package com.lagou.strings;

public class internDemo {
    public static void main(String[] args) {
        String s = new StringBuilder("a").append("ab").toString();
        System.out.println(s == s.intern());

        String s1 = "aab";
        System.out.println(s==s1);
    }
}
