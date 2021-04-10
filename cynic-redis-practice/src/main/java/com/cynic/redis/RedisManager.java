package com.cynic.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {

    private static JedisPool jedisPool;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMaxTotal(20);
        jedisPool = new JedisPool(jedisPoolConfig, "106.12.50.184", 7001, 2000, "cynic");
    }

    public static Jedis getJedisInstance() throws Exception {
        if (null != jedisPool) {
            return jedisPool.getResource();
        }
        throw new Exception("JedisPool Instance not init");
    }


//    public static void main(String[] args) {
//        JedisPool jedisPool = new JedisPool();
//        Jedis jedis = jedisPool.getResource();
//        jedis.eval("123");
//        boolean var5;
//        Long a = 1L;
//        try {
//            Object result = jedis.eval("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end", Collections.singletonList("1"), Collections.singletonList("21"));
//            var5 = a.equals(result);
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//    }

}
