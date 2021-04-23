package com.lagou.task14;

import com.sun.source.tree.Tree;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class TreeSetTest {
    public static void main(String[] args) {
        Set<Person> set = new TreeSet<>();
        set.add(new Person("11","boy",11));
        set.add(new Person("22","boy",11));
        System.out.println(set);
        Comparator<Person> comparator = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return 0;
            }
        };
    }
}
