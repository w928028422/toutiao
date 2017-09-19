package com.ayzl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Component
public class JedisAdapter implements InitializingBean{
    private JedisPool pool = null;
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost", 6379);
    }

    private Jedis getJedis(){
        return pool.getResource();
    }

    public long sadd(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        }catch (Exception e){
            logger.error("jedis异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long srem(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key, value);
        }catch (Exception e){
            logger.error("jedis异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        }catch (Exception e){
            logger.error("jedis异常" + e.getMessage());
            return false;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("jedis异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        }catch (Exception e){
            logger.error("jedis异常" + e.getMessage());
            return 0;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        }catch (Exception e){
            logger.error("jedis异常" + e.getMessage());
            return null;
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }


    /*public static void print(int index, Object object){
        System.out.println(String.format("%d, %s",index, object.toString()));
    }

    public static void main(String[] args){
        Jedis jedis = new Jedis();
        jedis.flushAll();
        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "hlloa");
        print(2, jedis.get("hlloa"));
        jedis.setex("yanzheng", 1, "dasdasf");
        print(3, jedis.get("yanzheng"));
        jedis.set("pv", "100");
        jedis.incr("pv");
        print(4, jedis.get("pv"));
        jedis.incrBy("pv", 5);
        print(5, jedis.get("pv"));

        String listName = "list";
        for(int i=0;i<10;i++){
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        print(6, jedis.lrange(listName, 0, 10));
        print(7, jedis.llen(listName));
        print(8, jedis.lpop(listName));
        print(9, jedis.lindex(listName,0));

        String userKey = "POJ";
        jedis.hset(userKey, "name", "qzw");
        jedis.hset(userKey, "age", "999");
        print(10, jedis.hgetAll(userKey));
        print(11, jedis.hexists(userKey,"email"));
        print(12, jedis.hkeys(userKey));
        print(13, jedis.hvals(userKey));
        jedis.hsetnx(userKey, "fff", "666");
        jedis.hsetnx(userKey, "name", "55555");
        jedis.hdel(userKey, "name");
        print(15, jedis.hgetAll(userKey));

        String set1 = "sset1", set2 = "sset2";
        for(int i=0;i<10;i++){
            jedis.sadd(set1, String.valueOf(i));
            jedis.sadd(set2, String.valueOf(i*2));
        }
        print(16, jedis.sdiff(set1, set2));
        print(17, jedis.sinter(set1, set2));
        print(18, jedis.smembers(set1));
        print(19, jedis.smembers(set2));
        print(20, jedis.sunion(set1, set2));
        print(21, jedis.sdiff(set2, set1));
        jedis.srem(set1, "5");
        print(22, jedis.sismember(set1, "5"));
        print(23, jedis.scard(set1));

        String rank = "rank";
        jedis.zadd(rank, 66, "dad");
        jedis.zadd(rank, 53, "dlaf");
        jedis.zadd(rank, 95, "saq");
        jedis.zadd(rank, 36, "qer");
        print(24, jedis.zcard(rank));
        print(25, jedis.zcount(rank, 50, 70));
        print(26, jedis.zscore(rank, "saq"));
        jedis.zincrby(rank, 10, "qer");
        print(27, jedis.zcount(rank,0,100));
        print(28, jedis.zrange(rank, 1, 3));
        print(29, jedis.zrevrange(rank, 1, 3));
        print(30, jedis.zrank(rank, "dad"));

        JedisPool pool = new JedisPool();
        for(int i=0;i<10;i++){
            Jedis j = pool.getResource();
            j.get("ss");
            System.out.println("sada" + i);
            j.close();
        }
    }*/
}
