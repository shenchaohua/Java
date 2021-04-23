package com.lagou.task09;

public interface Metal {


    public abstract void shine();

    public default void show(){
        System.out.println("show ");
    }
}
