package com.test.A;

import com.test.B.TestB;

public class TestA {
    private TestB b;

    public static int n = TestB.num();

    public TestA(){
//        b = new TestB();
        System.out.println("init A");
    }
    public static int num() {
        return n;
    }
}
