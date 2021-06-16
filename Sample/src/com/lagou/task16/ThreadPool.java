package com.lagou.task16;

import java.util.concurrent.*;

public class ThreadPool {
    public static void main(String[] args) {

        ExecutorService executorService1 =  Executors.newCachedThreadPool();
        ExecutorService executorService2 = Executors.newFixedThreadPool(1);
        ExecutorService executorService3 = Executors.newSingleThreadExecutor();
        ExecutorService executorService4 = new ThreadPoolExecutor(10,20,0L,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10));

        for ( int i=0 ; i<100;i++){
            executorService4.execute(new MyTask(i));
        }

    }
}
class MyTask implements Runnable{

    int i ;

    public MyTask() {
    }
    public MyTask(int i){
        this.i = i;
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"=="+i);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
