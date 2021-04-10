package com.cynic.redis;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisLock {


    public String getLock(String key) {
        try {
            Jedis jedis = RedisManager.getJedisInstance();
            String value = UUID.randomUUID().toString();
            if (jedis.setnx(key, value) == 1) {
                //获取锁成功
                return value;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLockBlock(String key, int timeout) {
        try {
            Jedis jedis = RedisManager.getJedisInstance();
            String value = UUID.randomUUID().toString();
            long end = System.currentTimeMillis() + timeout;
            while (System.currentTimeMillis() < end) {//阻塞
                if (jedis.setnx(key, value) == 1) {
                    //获取锁成功
                    return value;
                }
                TimeUnit.SECONDS.sleep(1);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean releaseLock(String key, String value) {
        try {
            Jedis jedis = RedisManager.getJedisInstance();
            while (true) {
                jedis.watch(key); //watch命令  监控一个或多个key,一旦这个key被修改或删除,后面事务中的操作就不会被执行;
                if (value.equals(jedis.get(key))) {//确保获得的锁和当前的redis存的锁时同一个
                    Transaction transaction = jedis.multi();
                    transaction.del(key);
                    List<Object> list = transaction.exec();//事务中的多个命令返回的结果集
                    if (CollectionUtils.isEmpty(list)) {
                        continue;
                    }
                    return true;
                }
                jedis.unwatch();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        RedisLock redisLock = new RedisLock();
        String lockValue = redisLock.getLockBlock("lock:cynic", 10000);
        if (!StringUtils.isEmpty(lockValue)) {
            System.out.println("获得锁成功");
        } else {
            System.out.println("获得锁失败");
        }
    }

}
