package com.zookeeper.zookeeper.zknativeapi;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cynic
 * @version 1.0
 * @createTime 2020/5/11 14:47
 */
public class SimpleDistributeLock {

    private static Logger logger = LoggerFactory.getLogger(SimpleDistributeLock.class);

    private static ZooKeeper zooKeeper;

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
        boolean hasGetLock = false;
        try {
            zooKeeper.create("/locks", "lock".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            hasGetLock = true;
        } catch (KeeperException e) {
            System.out.println("节点/locks已存在...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return hasGetLock;
    }


}
