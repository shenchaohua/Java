package com.lagou.task09;

public class AreaOuter {
    public int anInt = 1;

    public void show(){
        final int  a = 1;

        class Inner{
            public void show(){
//                a = 1;
                System.out.println(anInt+a);
            }
        }
        Inner ai = new Inner();

    }
}
