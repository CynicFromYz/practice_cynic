package com.netty.futrue;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/26 17:09
 */
public class JoinClient {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread();
        Thread thread2 = new Thread();
        thread2.join();
    }
}
