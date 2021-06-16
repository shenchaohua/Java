package com.lagou.task16;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class RunnableTest implements Runnable {
    public static void main(String[] args) {
        RunnableTest test = new RunnableTest();
        Thread thread = new Thread(test);
        Thread thread2 = new Thread(" s");
        thread.start();
        thread.run();




        new Thread(()->{
            System.out.println();
        }).start();

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("here");
    }
}
