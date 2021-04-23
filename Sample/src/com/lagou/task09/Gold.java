package com.lagou.task09;

public class Gold implements Metal,Money{

    @Override
    public void shine() {
        System.out.println("faguang");
    }

    @Override
    public void buy() {
        System.out.println("bus something");
    }

    public static void main(String[] args) {
        Metal mt = new Gold();
        mt.shine();
        mt.show();
    }
}
