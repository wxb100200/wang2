package com.base.wang.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wxb on 2019/3/5.
 */
public interface JedisClient {
    //---》》》》Hash

    String get(String key);//读取数据
    String set(String key, String value);//向redis中写入数据
    long incr(String key);
    /**
     * 确认一个key是否存在
     */
    Boolean exists(String key);
    /**
     *删除一个key
     */
    long del(String key);
    /**
     *删除多个key
     */
    long del(String... key);//删除数据
    /**
     * 返回值的类型
     */
    String type(String key);
    /**
     * 设定一个key的活动时间（s）
     */
    long expire(String key, int second);//设置缓存生存时间
    /**
     * 获得一个key的活动时间
     */
    long ttl(String key);
    /**
     * 将当前数据库中的key转移到有dbindex索引的数据库
     */
    Long move(String key, int dbindex);

    //---》》》》Hash
    /**
     *向名称为key的hash中添加元素field<—>value
     */
    long hset(String hkey, String key, String value);
    /**
     * 返回名称为key的hash中field对应的value
     */
    String hget(String hkey, String key);
    /**
     *返回名称为key的hash中field i对应的value
     */
    List<String> hmget(String hkey, String... field);
    /**
     *向名称为key的hash中添加元素field i<—>value i
     */
    String hmset(String hkey, Map<String, String> map);
    /**
     * 名称为key的hash中是否存在键为field的域
     */
    Boolean hexists(String hkey, String field);

    /**
     * 删除名称为key的hash中键为field的域
     */
    Long hdel(String hkey, String... field);
    /**
     * 返回名称为key的hash中元素个数
     */
    Long hlen(String hkey);

    /**
     * 返回名称为key的hash中所有键
     */
    Set<String> hkeys(String hkey);
    /**
     * 返回名称为key的hash中所有键对应的value
     */
    List<String> hvals(String hkey);
    /**
     * 返回名称为key的hash中所有的键（field）及其对应的value
     */
    Map<String, String> hgetAll(String hkey);

    //---》》》》List
    /**
     * 在名称为key的list尾添加一个值为value的元素
     */
    Long rpush(String key, String... strings);
    /**
     * 在名称为key的list头添加一个值为value的 元素
     */
    Long lpush(String key, String... strings);
    /**
     * 返回名称为key的list的长度
     */
    Long llen(String key);
    /**
     * 返回名称为key的list中start至end之间的元素（下标从0开始，下同）
     */
    List<String> lrange(String key, long start, long end);
    /**
     * 截取名称为key的list，保留start至end之间的元素
     */
    String ltrim(String key, long start, long end);
    /**
     * 返回名称为key的list中index位置的元素
     */
    String lindex(String key, long index);

    /***
     * 给名称为key的list中index位置的元素赋值为value
     */
    String lset(String key, long index, String value);

    /**
     * 删除count个名称为key的list中值为value的元素。count为0，删除所有值为value的元素，count>0      从头至尾删除count个值为value的元素，count<0从尾到头删除|count|个值为value的元素。
     */
    Long lrem(String key, long count, String value);

    /**
     * 返回并删除名称为key的list中的首元素
     */
    String lpop(String key);
    /**
     * 返回并删除名称为key的list中的尾元素
     */
    String rpop(String key);

    //---》》》》Set

    /**
     * 向名称为key的set中添加元素member
     */
    Long sadd(String key, String... member);
    /**
     * 删除名称为key的set中的元素member
     */
    Long srem(String key, String... member);
    /**
     * 随机返回并删除名称为key的set中一个元素
     */
    String spop(String key);
    /**
     * 返回名称为key的set的基数
     */
    Long scard(String key);
    /**
     * 测试member是否是名称为key的set的元素
     */
    Boolean sismember(String key, String member);
    /**
     * 随机返回名称为key的set的一个元素
     */
    String srandmember(String key);


}

















