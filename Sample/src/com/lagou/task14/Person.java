package com.lagou.task14;

import javax.lang.model.util.TypeKindVisitor14;
import java.io.FilenameFilter;

public class Person<T> implements Comparable<Person> {
    private String name;
    private T gender;
    private int age;

    public Person() {
    }

    public Person(String name, T gender, int age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getGender() {
        return gender;
    }

    public void setGender(T gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public <T1> void show(T1[] arr){
        for (T1 tt:arr){
            System.out.println(tt);
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                '}';
    }

    public static void main(String[] args) {
        Person person = new Person();
        person.getGender();
    }

    @Override
    public int compareTo(Person o) {
        return 0;
    }
}
