package com.lagou.classloader;

import java.lang.reflect.Method;

public class testClass {
    {

    }
    public static void main(String[] args) throws Exception {
        //这里取AppClassLoader的父加载器也就是ExtClassLoader作为MyClassLoaderCustom的jdkClassLoader
        MyClassLoaderCustom myClassLoaderCustom = new MyClassLoaderCustom(Thread.currentThread().getContextClassLoader().getParent());
        Class testAClass = myClassLoaderCustom.loadClass("test.TestA");
        Method mainMethod = testAClass.getDeclaredMethod("main", String[].class);
        Thread.currentThread().getContextClassLoader().getParent().loadClass("java.lang.String");
        mainMethod.invoke(null, new Object[]{args});
    }
}
