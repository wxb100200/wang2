package com.base.wang.service.impl;

import com.base.wang.entity.BasTest;
import com.base.wang.mapper.BasTestMapper;
import com.base.wang.service.BasTestService;
import com.base.wang.service.base.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class BasTestServiceImpl extends BaseServiceImpl<BasTest> implements BasTestService {
    private static final Logger logger = LoggerFactory.getLogger(BasTestServiceImpl.class);
    @Autowired
    private BasTestMapper basTestMapper;

    @Override
    public BasTest selectById(Integer id) {
        logger.info("---->>>>>>>id:"+id);
        BasTest test = basTestMapper.selectByPrimaryKey(id);
        return test;
    }

    @Override
    public BasTest findByUserName(String userName) {
        logger.info("---->>>>>>>userName:"+userName);
        BasTest test = basTestMapper.findByUserName(userName);
        return test;
    }
}
