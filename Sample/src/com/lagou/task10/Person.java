package com.lagou.task10;

import java.util.stream.Stream;

@MyAnnotation()
public class Person {
    /**
    test 1
     */
    private String name;
    /**
    test 2
     */
    private int age;
    /**
        test 2
         */

    public Person() {
    }

    /**
     * test
     * @param name
     * @param age
     */
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }
    /**
        test 2
         */
    public void setName(String name) {
        this.name = name;
    }
    /**
        test 2
         */
    public int getAge() {
        return age;
    }
    /**
        test 2
         */
    public void setAge(int age) {
        this.age = age;
    }
}
