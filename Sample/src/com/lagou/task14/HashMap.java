package com.lagou.task14;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HashMap {
    public static void main(String[] args) {
        Map<String,String> map = new java.util.HashMap<>();
        map.put("s","b");
        map.put("nb","b");
        Set<String> strings = map.keySet();
        for (String string:strings){
            System.out.println(string);
//            map.remove(string);
        }
        List<Integer> sss = Arrays.asList(new Integer[5]);
    }
}
