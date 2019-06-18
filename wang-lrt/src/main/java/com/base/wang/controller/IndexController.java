package com.base.wang.controller;

import com.base.wang.config.InitConfig;
import com.base.wang.entity.BasTest;
import com.base.wang.mapper.BasTestMapper;
import com.base.wang.service.BasTestService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wxb on 2019/4/13.
 */
@RestController
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private BasTestMapper basTestMapper;
    @Autowired
    private BasTestService basTestService;

    @GetMapping(value = "/test")
    public String index(){
        return "hello springBoot2.0!!!!";
    }

    @RequestMapping("/findOne")
    public String findOne(){
        BasTest test = basTestMapper.selectByPrimaryKey(1);
        return new StringBuilder().append("用户名:").append(test.getUsername()).toString();
    }
    @RequestMapping("/findOneVo")
    public String findOneVo(Integer id){
        BasTest test = basTestService.selectById(id);
        return new StringBuilder().append("用户名:").append(test.getUsername()).toString();
    }
    @RequestMapping("/findData")
    public String findOneVo(BasTest data){
        Integer id=data.getId();
        BasTest test = basTestService.selectById(id);
        return new StringBuilder().append("用户名:").append(test.getUsername()).toString();
    }

    @RequestMapping("/findOneData")
    public Object findOneData(){
//        BasTest test =basTestMapper.selectByPrimaryKey(3);
//        BasTest test =basTestMapper.selectById(3);
        BasTest test =basTestMapper.selectByIdVo(3);
        return test;
    }
    /**
     * page 当前页数<br>
     * size 当前展示的数据<br>
     */
    @RequestMapping("/pageInfo")
    public PageInfo<BasTest> findUserList(int page, int size) {
        // 开启分页插件,放在查询语句上面
        PageHelper.startPage(page, size);
        List<BasTest> listUser = basTestMapper.selectAll();
        // 封装分页之后的数据
        PageInfo<BasTest> pageInfoUser = new PageInfo<BasTest>(listUser);
        return pageInfoUser;
    }
    /**
     * page 当前页数<br>
     * size 当前展示的数据<br>
     */
    @RequestMapping("/pageInfoVo")
    public PageInfo<BasTest> findUserListVo(int page, int size) {
        // 开启分页插件,放在查询语句上面
        PageHelper.startPage(page, size);
        List<BasTest> listUser = basTestService.selectPage(page,size);
        // 封装分页之后的数据
        PageInfo<BasTest> pageInfoUser = new PageInfo<BasTest>(listUser);
        return pageInfoUser;
    }

    @GetMapping(value = "/log")
    public String log(){
        logger.info("---->>>>>>>>>>>>>>>>>dsfdsfsadfsadfasdfsadf");
        logger.error("---->>>>>>>>>>>>>>>>>错误日志。。。。。");
        return "hello springBoot2.0!!!!";
    }
    @GetMapping(value = "/initConfig")
    public String initConfig(){
        logger.info("---->>>>>>>>>>>>>>>>>dsfdsfsadfsadfasdfsadf");
        return InitConfig.UPLOAD_PATH;
    }
}
