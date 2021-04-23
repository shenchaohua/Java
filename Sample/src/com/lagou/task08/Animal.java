package com.lagou.task08;

public class Animal {
    private String name;



    public Animal(String name, String color) {
        setName(name);
        setColor(color);
    }

    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void show(){
        System.out.println("name is " + getName()+" color is " + getColor());
    }
}
