package com.zookeeper.zookeeper.zknativeapi;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

/**
 * @author cynic
 * @version 1.0
 * @createTime 2020/5/11 15:28
 */
public class ComplexDistributeLock {
    private static Logger logger = LoggerFactory.getLogger(SimpleDistributeLock.class);

    private static ZooKeeper zooKeeper;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            zooKeeper = ZkClient.getZkInstance();
        } catch (Exception e) {
            logger.error("zk客户端实例化发生错误,message:{}", e.getMessage());
            return;
        }
        boolean hasGetLock = tryLock();
        if (hasGetLock) {
            System.out.println("获取锁成功...");
        } else {
            System.out.println("获取锁失败...");
        }
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            logger.error("zooKeeper关闭异常,message:{}", e.getMessage());
        }
    }

    public static boolean tryLock() {
        try {
            String node = zooKeeper.create("/locks/", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            List<String> nodeList = zooKeeper.getChildren("/locks", true);
            SortedSet<String> nodeSet = new TreeSet<>();
            for (String nodeSequenceStr : nodeList) {
                nodeSet.add("/locks/" + nodeSequenceStr);
            }
            if (node.equals(nodeSet.first())) {
                return true;
            }
            SortedSet<String> lessNodeSet = nodeSet.headSet(node);
            if (!lessNodeSet.isEmpty()) {
                String lastNode = lessNodeSet.last();
                zooKeeper.exists(lastNode, watchedEvent -> {
                    if (Watcher.Event.EventType.NodeDeleted.equals(watchedEvent.getType())) {
                        countDownLatch.countDown();
                    }
                });
            }
            countDownLatch.await();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
