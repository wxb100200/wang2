package com.base.wang.service.impl;

import com.base.wang.entity.BasConfigParam;
import com.base.wang.mapper.BasConfigParamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("configParamService")
public class ConfigParamService {
    @Autowired
    private BasConfigParamMapper configParamMapper;
    /**
     * 查询所有配置数据
     */
    public List<BasConfigParam> findAllConfig() {
        List<BasConfigParam> configParamList = configParamMapper.findAll();
        return configParamList;
    }
}
