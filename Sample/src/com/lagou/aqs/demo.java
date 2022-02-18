

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class demo {
    AtomicReference<Thread> reference = null;

    public demo() {
        this.reference = new AtomicReference<>();;
    }

    public void lock() {
        while (! reference.compareAndSet(null, Thread.currentThread())) {

        }
    }
    public void unlock() {
        reference.compareAndSet(Thread.currentThread(), null);
    }

    public void deadLock() {
        demo demo = new demo();

        new Thread(() -> {
            demo.lock();
            System.out.println(Thread.currentThread().getName()+"start");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            demo.unlock();
            System.out.println(Thread.currentThread().getName()+"finished");
        }, "aaa").start();

        new Thread(() -> {
            demo.lock();
            System.out.println(Thread.currentThread().getName()+"start");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            demo.unlock();
            System.out.println(Thread.currentThread().getName()+"finished");
        }, "aaa").start();
    }

    public static void main(String[] args) throws InterruptedException {
        HashSet<String> strings = new HashSet<>();
        String a = "";
        int i  = 0;
        Random random = new Random();
        while (true) {
            a += i++;
            strings.add(a.intern());
        }

    }


}
