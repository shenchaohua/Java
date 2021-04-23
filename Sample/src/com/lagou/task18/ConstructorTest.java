package com.lagou.task18;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorTest {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class c1 = Class.forName("com.lagou.task18.ConstructorTest");
        Constructor constructor = c1.getConstructor();
        Object test = constructor.newInstance("test");
        System.out.println(test);
    }
}
