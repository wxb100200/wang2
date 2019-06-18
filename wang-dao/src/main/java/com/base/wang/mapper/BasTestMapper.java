package com.base.wang.mapper;

import com.base.wang.entity.BasTest;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
@Component
public interface BasTestMapper extends Mapper<BasTest>, MySqlMapper<BasTest> {
    BasTest selectByIdVo(int i);

    BasTest findByUserName(String userName);
}