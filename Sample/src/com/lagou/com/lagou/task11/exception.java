package com.lagou.com.lagou.task11;

import java.util.concurrent.*;

public class exception {
    public static void main(String[] args) {
        ExecutorService service = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(10))   {

            //重写afterExecute方法
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                if (t != null) { //这个是excute提交的时候
                    System.out.println("afterExecute里面获取到异常信息" + t.getMessage());
                }

//如果r的实际类型是FutureTask 那么是submit提交的，所以可以在里面get到异常
                if (r instanceof FutureTask) {
                    try {
                        Future<?> future = (Future<?>) r;
                        future.get();
                    } catch (Exception e) {
                        System.out.println("future里面取执行异常" + e);
                    }
                }
            }
        };

        //2.提交任务
        Future<?> future = service.submit(() -> {
            int i = 1 / 0;
        });
        try {
            Object o = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        service.shutdown();
    }
}
