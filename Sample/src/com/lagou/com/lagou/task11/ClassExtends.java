package com.lagou.com.lagou.task11;

class Animal{
    String name;
    public void play(){
        System.out.println(speak());
    }
    public String speak(){
        return "my name is "+name;
    }
    public String getName(){
        return name;
    }

    public Animal(String name) {
        this.name = name;
    }
}
class Dog extends Animal{
    String gender;

    @Override
    public String speak() {
        return super.speak()+" and my gender is"+gender;
    }

    public Dog(String name, String gender) {
        super(name);
        this.gender = gender;
    }
}

public class ClassExtends {
    public static void main(String[] args) {
        Dog dog = new Dog("zhangdan", "22");
        System.out.println(dog.speak());
        dog.play();
        String a = "ab";
        String b = "a"+"b";
        System.out.println(a==b);
    }
}
