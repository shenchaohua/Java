import com.test.B.TestB;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest extends Thread {
    @Override
    public void run() {
        System.out.println("here" + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
//        new test().start();
//        System.out.println(Thread.currentThread().getName());
//        new Thread(){
//            @Override
//            public void run() {
//                lock.lock();
//                System.out.println("here");
//            }
//        }.start();
//        new Thread(() -> {lock.lock();System.out.println(Thread.currentThread().getName());}).start();
//        new Thread(() -> {lock.lock();System.out.println(Thread.currentThread().getName());}).start();
        new Thread(new FutureTask(()-> {
            // logic
            System.out.println(111);
            return 1;
        })).start();
        ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();
        HashMap<Object, Object> hashMap = new HashMap<>();
        AtomicInteger atomicInteger = new AtomicInteger(5);
        CyclicBarrier barrier = new CyclicBarrier(2);
//        new CountDownLatch();
//        new FutureTask<>();
        ExecutorService executorService = new ThreadPoolExecutor(2,2,60L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2));
        AtomicInteger cnt = new AtomicInteger();

        ThreadLocal<Integer> num = new ThreadLocal<>();

//        executorService.submit(()->{ for (;cnt.get()<=99;) { if (cnt.get() %2==0) {
//            System.out.println(cnt);System.out.flush();cnt.getAndIncrement();} }});
//        executorService.submit(()->{ for (;cnt.get()<=100;) { if (cnt.get() %2==1) {
//            System.out.println(cnt);System.out.flush(); cnt.getAndIncrement();} }});
//        executorService.shutdown();

        new Thread(()-> {num.get();}).start();
        new Thread(()-> num.get()).start();
        num.set(1);
        Integer a = num.get();
        System.out.println(TestB.n);
    }

}

