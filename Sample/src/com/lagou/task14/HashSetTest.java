package com.lagou.task14;

import java.util.HashSet;
import java.util.Set;

public class HashSetTest {
    public static void main(String[] args) {
        Set<String> s1 = new HashSet<>();
        s1.add("1");
        boolean b1 = s1.add("1");
        System.out.println(b1);
    }
    
}
