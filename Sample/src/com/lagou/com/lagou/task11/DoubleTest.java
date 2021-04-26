package com.lagou.com.lagou.task11;

import java.math.BigDecimal;
public class DoubleTest {
    public static void main(String[] args){
        float a = 1.13234f;
        System.out.println(a);
        System.out.println(0.1+0.2);
        System.out.println(new BigDecimal("0.1").add(new BigDecimal("0.2")));
    }
}
