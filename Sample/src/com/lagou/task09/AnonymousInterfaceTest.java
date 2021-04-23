package com.lagou.task09;

public class AnonymousInterfaceTest {

    public static void show(){

    }

    public static void main(String[] args) {
        show();
        AnonymousInterface ait = new AnonymousInterface() {
            @Override
            public void show() {

            }
        };
    }
}
