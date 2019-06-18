package com.base.wang.controller;

import com.base.wang.entity.BasTest;
import com.base.wang.service.RedisService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wxb on 2019/3/5.
 */
@RestController
@RequestMapping("/redis")
public class RedisController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RedisController.class);
    @Autowired
    private RedisService redisService;
    @ResponseBody
    @RequestMapping("/findById")
    public Object findById(Integer id){
        BasTest test = redisService.findById(id);
        return test;
    }
    /**
     * 初始化商品
     * @return
     */
    @ResponseBody
    @RequestMapping("/initStores")
    public Object initStores(){
        return redisService.initStores();
    }

    /**
     * 抢购商品
     * @return
     */
    @ResponseBody
    @RequestMapping("/rushToBuy")
    public Object rushToBuy(){
        return redisService.rushToBuy();
    }
}
