package com.netty.chat.testpackage;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestLock {

    static ThreadPoolExecutor executors = (ThreadPoolExecutor) Executors.newFixedThreadPool(25);

    static ThreadPoolExecutor executors2 = (ThreadPoolExecutor) Executors.newFixedThreadPool(25);

    public static Object lockObj = new Object();

    public static void main(String[] args) throws InterruptedException {
        executors2.submit(()->{
            try {
                while (true){
                    TimeUnit.SECONDS.sleep(1);
                    CuxiaoStatic.pri();
                    synchronized (CuxiaoStatic.lockObj){
                        System.out.println("XXXXXXXXXXXXXXXXXXXXXXX");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        for (int i = 0; i < 3; i++) {
            synchronized (CuxiaoStatic.lockObj) {
                System.out.println("对象lockObj加锁了");
                TimeUnit.SECONDS.sleep(10);
                executors.submit(() -> {
                    System.out.println("111111111111111");
                });
            }

        }

    }

    private void getM(Integer a){

    }

    private String getM(String a){
        return "";
    }
}
