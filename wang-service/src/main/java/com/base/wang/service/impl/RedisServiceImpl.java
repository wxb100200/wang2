package com.base.wang.service.impl;

import com.base.wang.common.PageReturn;
import com.base.wang.entity.BasTest;
import com.base.wang.mapper.BasTestMapper;
import com.base.wang.service.JedisClient;
import com.base.wang.service.RedisService;
import com.base.wang.service.base.BaseServiceImpl;
import com.base.wang.util.JsonUtil;
import com.base.wang.util.StringUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by wxb on 2019/3/5.
 */
@Service("redisService")
public class RedisServiceImpl extends BaseServiceImpl<BasTest> implements RedisService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
    static final int stores = 1000;//库存量
    static final String storesName = "stores";//抢购商品存放仓库名称
    static final String storesCountKey = "storesCountKey";//记录以往仓库中存放商品的数量的KEY
    static final String setSuccessName = "setSuccessName";//记录抢购成功用户的set集合KEY
    static final String setFailName = "setFailName";//记录抢购失败用户的set集合KEY

    @Autowired
    private BasTestMapper testMapper;
    @Autowired
    private JedisClient jedisClient;

    @Override
    public BasTest findById(Integer id) {
        String dataStr=jedisClient.hget("wangxiaobing",id+"");
        if(StringUtil.isEmpty(dataStr)){
            System.out.println("--->>>>1111");
            BasTest test= testMapper.selectByIdVo(id);
            if(test==null)return null;
            jedisClient.hset("wangxiaobing",id+"", JsonUtil.toJson(test));
            return test;
        }else {
            System.out.println("--->>>>22222");
            BasTest test= JsonUtil.json2Object(dataStr, BasTest.class);
            return test;
        }

    }

    @Override
    public Object initStores() {
        ////初始化
        jedisClient.del(storesName, setSuccessName, setFailName);//删除仓库，删除抢购成功队列，删除抢购失败队列
        jedisClient.set(storesCountKey, stores + "");
        for (int i = 1; i <= stores; i++) {
            String storeProp = i + ":" + UUID.randomUUID().toString();//模拟   序号:ID
            jedisClient.rpush(storesName, storeProp);
        }
        return PageReturn.success();
    }

    @Override
    public Object rushToBuy(){
        String userifo = UUID.randomUUID().toString();
        //redis list取值
        String ls = jedisClient.lpop(storesName);
        //取值成功，说明拿到了商品,即抢购成功
        if (ls != null) {
            String[] er = ls.split(":");
            int num = Integer.valueOf(er[0]);//商品序号
            String ID = er[1];//商品ID

                    /* 抢购成功业务逻辑 */
            jedisClient.sadd(setSuccessName, userifo);
            System.out.println("用户：" + userifo + "抢购成功，当前抢购成功人数:"
                    + num + "------抢购成功商品ID:" + ID);
            try {
                Thread.sleep(900);//模拟业务执行时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return PageReturn.success("用户：" + userifo + "抢购成功，当前抢购成功人数:"
                    + num + "------抢购成功商品ID:" + ID);

        } else {
            jedisClient.sadd(setFailName, userifo);
            System.out.println("用户：" + userifo + "抢购失败，库存以空");
            try {
                Thread.sleep(400);//模拟业务执行时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return PageReturn.fail("用户：" + userifo + "抢购失败，库存以空");
        }
    }
}
