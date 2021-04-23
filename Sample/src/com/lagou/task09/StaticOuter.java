package com.lagou.task09;

public class StaticOuter {
    private int cnt = 0;
    private static int snt = 1;

    public static class StaticInner{
        public void show(){
            System.out.println(snt);
        }
    }

    public static void main(String[] args) {

    }
}
