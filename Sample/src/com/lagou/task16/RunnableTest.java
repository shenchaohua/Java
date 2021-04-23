package com.lagou.task16;

public class RunnableTest implements Runnable {
    public static void main(String[] args) {
        RunnableTest test = new RunnableTest();
        Thread thread = new Thread(test);
        Thread thread2 = new Thread(" s");
        thread.start();
        thread.run();


        new Thread(new RunnableTest(){
            @Override
            public void run(){
                System.out.println();
            }
        }).start();

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
