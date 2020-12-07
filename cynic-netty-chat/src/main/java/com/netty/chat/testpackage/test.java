package com.netty.chat.testpackage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class test {

    static ThreadPoolExecutor executors = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        ThreadPoolExecutor exportExecutor = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), new ThreadPoolExecutor.AbortPolicy());
        List<Future> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            int finalI = i;
            Future future = exportExecutor.submit((Callable<Object>) () -> {
                System.out.println("hello world__" + System.currentTimeMillis() + "__" + (finalI + 1));
                TimeUnit.SECONDS.sleep(30);
                System.out.println("等待30s结束...");
                return "1";
            });
            list.add(future);
        }
        System.out.println(exportExecutor.getQueue().size());
        System.out.println(list.size());
        System.out.println(exportExecutor.getCorePoolSize());
        System.out.println(exportExecutor.getMaximumPoolSize());
        System.out.println(exportExecutor.getActiveCount());
        AtomicInteger count = new AtomicInteger();
        for (Future future : list) {
            executors.submit(() -> {
                try {
                    System.out.println(future.get(10, TimeUnit.SECONDS) + "__" + (count.incrementAndGet()));
                } catch (Exception e) {
                    future.cancel(true);
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    System.out.println("剩余活跃线程数:" + exportExecutor.getActiveCount());
                }
            });
        }
    }
}
