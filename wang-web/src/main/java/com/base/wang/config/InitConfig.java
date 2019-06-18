package com.base.wang.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by wxb on 2019/4/15.
 */
@Component
public class InitConfig implements InitializingBean {
    @Value("${upload.path}")
    private String upload_path ;

    public static String UPLOAD_PATH;


    @Override
    public void afterPropertiesSet() throws Exception {
        UPLOAD_PATH = upload_path;
    }
}
