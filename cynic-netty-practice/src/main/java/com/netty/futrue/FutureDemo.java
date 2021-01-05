package com.netty.futrue;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.concurrent.*;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/27 10:46
 */
public class FutureDemo {

    public static final int SLEEP_GAP = 500;

    public static String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 5000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(50));

    static class HotWaterJob implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
            System.out.println("洗好茶壶");
            System.out.println("灌上凉水");
            System.out.println("放在火上");
            //线程睡眠一段时间,代表烧水过程.
            Thread.sleep(2000);
            System.out.println("水开了");
            System.out.println("烧水线程执行结束");
            return true;
        }
    }

    static class WashJob implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
            System.out.println("洗茶壶");
            System.out.println("洗茶杯");
            System.out.println("拿茶叶");
            //线程睡眠一段时间,代表清洗过程
            Thread.sleep(2000);
            System.out.println("清洗完成");
            System.out.println("清洗线程执行结束");
            return true;
        }
    }

    public static void drinkTea(boolean isWaterOk, boolean isCupOk) {
        if (isWaterOk && isCupOk) {
            System.out.println("喝茶了");
        } else if (!isWaterOk) {
            System.out.println("没有水");
        } else if (!isCupOk) {
            System.out.println("没有杯子");
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Boolean> hotWaterFuture = new FutureTask<>(new HotWaterJob());
        Thread waterThread = new Thread(hotWaterFuture, "Thread-烧水");
        FutureTask<Boolean> washFuture = new FutureTask<>(new WashJob());
        Thread washThread = new Thread(washFuture, "Thread-清洗");
        waterThread.start();
        washThread.start();
        Thread.currentThread().setName("主线程");
        boolean waterOk = hotWaterFuture.get();
        boolean cupOk = washFuture.get();
        drinkTea(waterOk, cupOk);
        System.out.println("运行结束");
    }

}
