package com.lagou.task14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CollectionTest {

    public static void main(String[] args) {
        Collection c1 = new ArrayList();
        System.out.println(c1);
        c1.add(new String("hello"));
        c1.add(Integer.valueOf(1));
        c1.add(Integer.valueOf(1));

        Collection c2 = new ArrayList();
        c2.add(1);
        c1.addAll(c2);
        System.out.println(c1);
        System.out.println(c1.contains(1));

        c1.remove(1);
        System.out.println(c1);


        int[] a = new int[]{1,2,3,4};
        for (int i: a
             ) {
            a[2]=1;
            System.out.println(i);
        }

        List ss = new LinkedList();
        ss.add(0,1);
    }
}
