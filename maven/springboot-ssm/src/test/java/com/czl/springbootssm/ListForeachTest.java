package com.czl.springbootssm;

import java.util.ArrayList;
import java.util.List;

public class ListForeachTest {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        strings.add("123");
        strings.add("1234");
        strings.forEach(one->{
            if (one.equals("123")){
                return;
            }
            System.out.println(one);
        });
    }
}
