package com.lagou.com.lagou.task11;

import java.util.ArrayList;
import java.util.Arrays;

public class lambdaTest {
    public static void test(){
        Integer[] a = new Integer[]{1,2};
        ArrayList<Integer> integers = new ArrayList<Integer>(Arrays.asList(a));
        integers.forEach(x->{
            System.out.println(x);
            throw new RuntimeException();
        });
    }
    public static void main(String[] args){
        test();
    }
}
