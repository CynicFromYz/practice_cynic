package com.netty.futrue;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/26 17:15
 */
public class JoinDemo {
    public static final int SLEEP_GAP = 500;

    public static String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }

    static class HotWaterThread extends Thread {
        public HotWaterThread() {
            super("Thread--烧水");
        }

        public void run() {
            System.out.println("洗好水壶---");
            System.out.println("灌上凉水---");
            System.out.println("放在火上---");
            //线程睡眠一段时间 代表烧水中
            try {
                Thread.sleep(SLEEP_GAP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("水开了---");
        }
    }

    static class WashThread extends Thread {
        public WashThread() {
            super("Thread--清洗");
        }

        public void run() {
            System.out.println("洗茶壶---");
            System.out.println("洗茶杯---");
            System.out.println("拿茶叶---");
            //线程睡眠一段时间 代表清洗中
            try {
                Thread.sleep(SLEEP_GAP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("清洗完成---");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread hotThread = new HotWaterThread();
        Thread washThread = new WashThread();
        hotThread.start();
        washThread.start();
        hotThread.join();
        washThread.join();
        Thread.currentThread().setName("主线程---");
        System.out.println("运行结束---");
    }
}
