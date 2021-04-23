package com.lagou.task16;

import java.util.concurrent.locks.ReentrantLock;

public class ThreadComminicateTest implements Runnable {
    private int cnt = 1;
    static ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        while(true){
//            synchronized (this) {
                lock.lock();
                notify();
                if(cnt<100){
                    System.out.println("thread id is "+ Thread.currentThread().getName() + "" + cnt);
                    try {
                        Thread.sleep(100);
                        cnt++;
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    break;
                }
                lock.unlock();
//            }
        }
    }

    public static void main(String[] args) {
        ThreadComminicateTest test = new ThreadComminicateTest();
        Thread t1 = new Thread(test);
        t1.start();
        Thread t2= new Thread(test);
        t2.start();
    }
}
