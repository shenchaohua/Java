package com.lagou.com.lagou.task11;

public class StringRegTest {
    public static void main(String[] args) {
        String str = "12345";
        System.out.println(str.matches("[0-9]{5}$"));
    }
}
