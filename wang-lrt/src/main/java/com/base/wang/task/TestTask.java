package com.base.wang.task;

import org.springframework.stereotype.Component;


/**
 * 测试定时任务
 */
@Component
public class TestTask {

//    @Scheduled(cron = "0/5 * * * * ? ")
    public void startTask() {
        System.out.println("--->>>>>我是定时任务开始了"+System.currentTimeMillis());
    }
}
