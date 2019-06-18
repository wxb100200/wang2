package com.base.wang.service;


import com.base.wang.entity.BasTest;
import com.base.wang.service.base.BaseService;

/**
 * Created by wxb on 2019/4/15.
 */
public interface BasTestService extends BaseService<BasTest> {
    BasTest selectById(Integer id);

    BasTest findByUserName(String userName);
}
