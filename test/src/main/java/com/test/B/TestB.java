package com.test.B;

import com.test.A.TestA;

public class TestB {
    private TestA a;

    public static int n = TestA.num();

    public TestB(){
        a = new TestA();
        System.out.println("testB init");
    }
    public static int num() {
        return n;
    }
    public static void main(String[] args) {
        System.out.println(n);
    }

}
