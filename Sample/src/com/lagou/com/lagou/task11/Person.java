package com.lagou.com.lagou.task11;

public class Person {
    String name;
    Person(){

    }
    Person(String name1){
        name = name1;
    }
    public static void main(String[] args){
        Person person = new Person("test");
        System.out.println(person.name );
    }
}
