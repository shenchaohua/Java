package com.lagou.task14;

import java.util.LinkedList;
import java.util.List;

public class ListTest {
    public static void main(String[] args) {
        List<String> list = new LinkedList();
        list.add("one");

        List<Animal> list1 = new LinkedList<>();
        List<Dog> list2 = new LinkedList<>();
        List list3 = new LinkedList<>();
        list3 = list2;
        List<Object> list4 = new LinkedList<>();
        list3.add("1");
        System.out.println(list3);
    }
}
