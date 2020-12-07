package com.netty.chat.testpackage;

import java.util.concurrent.*;

public class FutrueCancelTest {

    static ExecutorService pool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws InterruptedException {
        final Thread[] thread = {new Thread()};
        Future future = pool.submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                int count = 0;
                thread[0] = Thread.currentThread();
                while (true) {
                    System.out.println("线程没有终止! 打印次数:" + (++count));
                }
            }
        });
        TimeUnit.SECONDS.sleep(1);
//        future.cancel(true);
        thread[0].stop();
    }
}
