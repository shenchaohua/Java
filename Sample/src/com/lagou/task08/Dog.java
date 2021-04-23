package com.lagou.task08;

public class Dog extends Animal {
    private int tooth;

    public Dog(String name, String color, int tooth) {
        super(name,color);
        this.tooth = tooth;
    }

    public int getTeeth() {
        return tooth;
    }

    public void setTeeth(int teeth) {
        this.tooth = teeth;
    }

//    @Override
//    public void show() {
//        super.show();
//        System.out.println("tooth is " + getTeeth());
//    }
}
