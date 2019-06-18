package com.base.wang.service.impl;


import com.base.wang.service.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("jedisClient")
public class JedisClientSingle implements JedisClient {

    @Autowired
    private JedisPool jedisPool;

    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String string = jedis.get(key);
        jedis.close();
        return string;
    }

    @Override
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String string = jedis.set(key, value);
        jedis.close();
        return string;
    }
    @Override
    public long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.incr(key);
        jedis.close();
        return result;
    }

    @Override
    public Boolean exists(String key) {
        Jedis jedis = jedisPool.getResource();
        Boolean result = jedis.exists(key);
        jedis.close();
        return result;
    }

    @Override
    public long del(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.del(key);
        jedis.close();
        return result;
    }

    @Override
    public long del(String... key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.del(key);
        jedis.close();
        return result;
    }

    @Override
    public String type(String key) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.type(key);
        jedis.close();
        return result;
    }

    @Override
    public long expire(String key, int second) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.expire(key,second);
        jedis.close();
        return result;
    }

    @Override
    public long ttl(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.ttl(key);
        jedis.close();
        return result;
    }

    @Override
    public Long move(String key, int dbindex) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.move(key,dbindex);
        jedis.close();
        return result;
    }

    //---》》》》List
    @Override
    public long hset(String hkey, String key, String value) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hset(hkey, key, value);
        jedis.close();
        return result;
    }
    @Override
    public String hget(String hkey, String key) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hget(hkey, key);
        jedis.close();
        return result;
    }

    @Override
    public List<String> hmget(String hkey, String... field) {
        Jedis jedis = jedisPool.getResource();
        List<String> result = jedis.hmget(hkey, field);
        jedis.close();
        return result;
    }

    @Override
    public String hmset(String hkey, Map<String, String> map) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hmset(hkey, map);
        jedis.close();
        return result;
    }

    @Override
    public Boolean hexists(String hkey, String field) {
        Jedis jedis = jedisPool.getResource();
        Boolean result = jedis.hexists(hkey, field);
        jedis.close();
        return result;
    }

    @Override
    public Long hdel(String hkey, String... field) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hdel(hkey, field);
        jedis.close();
        return result;
    }

    @Override
    public Long hlen(String hkey) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hlen(hkey);
        jedis.close();
        return result;
    }

    @Override
    public Set<String> hkeys(String hkey) {
        Jedis jedis = jedisPool.getResource();
        Set<String> result = jedis.hkeys(hkey);
        jedis.close();
        return result;
    }

    @Override
    public List<String> hvals(String hkey) {
        Jedis jedis = jedisPool.getResource();
        List<String> result = jedis.hvals(hkey);
        jedis.close();
        return result;
    }

    @Override
    public Map<String, String> hgetAll(String hkey) {
        Jedis jedis = jedisPool.getResource();
        Map<String, String> result = jedis.hgetAll(hkey);
        jedis.close();
        return result;
    }


    //---》》》》List
    @Override
    public Long rpush(String key, String... strings) {
        Jedis jedis = jedisPool.getResource();
        Long result=jedis.rpush(key,strings);
        jedis.close();
        return result;
    }

    @Override
    public Long lpush(String key, String... strings) {
        Jedis jedis = jedisPool.getResource();
        Long result=jedis.lpush(key,strings);
        jedis.close();
        return result;
    }

    @Override
    public Long llen(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result=jedis.llen(key);
        jedis.close();
        return result;
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        List<String> list=jedis.lrange(key,start,end);
        jedis.close();
        return list;
    }

    @Override
    public String ltrim(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        String result=jedis.ltrim(key,start,end);
        jedis.close();
        return result;
    }

    @Override
    public String lindex(String key, long index) {
        Jedis jedis = jedisPool.getResource();
        String result=jedis.lindex(key,index);
        jedis.close();
        return result;
    }

    @Override
    public String lset(String key, long index, String value) {
        Jedis jedis = jedisPool.getResource();
        String result=jedis.lset(key,index,value);
        jedis.close();
        return result;
    }

    @Override
    public Long lrem(String key, long count, String value) {
        Jedis jedis = jedisPool.getResource();
        Long result=jedis.lrem(key,count,value);
        jedis.close();
        return result;
    }

    @Override
    public String lpop(String key) {
        Jedis jedis = jedisPool.getResource();
        String result=jedis.lpop(key);
        jedis.close();
        return result;
    }

    @Override
    public String rpop(String key) {
        Jedis jedis = jedisPool.getResource();
        String result=jedis.rpop(key);
        jedis.close();
        return result;
    }
    //---》》》》Set
    @Override
    public Long sadd(String key, String... member) {
        Jedis jedis = jedisPool.getResource();
        Long result=jedis.sadd(key,member);
        jedis.close();
        return result;
    }

    @Override
    public Long srem(String key, String... member) {
        Jedis jedis = jedisPool.getResource();
        Long result=jedis.srem(key,member);
        jedis.close();
        return result;
    }

    @Override
    public String spop(String key) {
        Jedis jedis = jedisPool.getResource();
        String result=jedis.spop(key);
        jedis.close();
        return result;
    }

    @Override
    public Long scard(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result=jedis.scard(key);
        jedis.close();
        return result;
    }

    @Override
    public Boolean sismember(String key, String member) {
        Jedis jedis = jedisPool.getResource();
        Boolean result=jedis.sismember(key,member);
        jedis.close();
        return result;
    }

    @Override
    public String srandmember(String key) {
        Jedis jedis = jedisPool.getResource();
        String result=jedis.srandmember(key);
        jedis.close();
        return result;
    }


}