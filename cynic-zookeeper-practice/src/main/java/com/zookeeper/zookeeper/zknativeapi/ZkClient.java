package com.zookeeper.zookeeper.zknativeapi;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author cynic
 * @version 1.0
 * @createTime 2020/5/11 13:52
 */
public class ZkClient {

    private final static String ZK_ADDR = "106.12.50.184:2181";

    private final static int SESSION_TIMEOUT = 5000;

    private static volatile ZooKeeper zooKeeper;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private ZkClient() {
    }

    public static ZooKeeper getZkInstance() throws Exception {
        if (zooKeeper == null) {
            synchronized (ZkClient.class) {
                if (zooKeeper == null) {
                    zooKeeper = new ZooKeeper(ZK_ADDR, SESSION_TIMEOUT, watchedEvent -> {
                        if (Watcher.Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                            System.out.println("zookeeper客户端连接成功...");
                            countDownLatch.countDown();
                        }
                    });
                    countDownLatch.await(SESSION_TIMEOUT, TimeUnit.MILLISECONDS);
                }
            }
        }
        return zooKeeper;
    }


}
