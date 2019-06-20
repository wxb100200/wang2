package com.base.wang.config;

import com.base.wang.common.Global;
import com.base.wang.entity.BasConfigParam;
import com.base.wang.service.impl.ConfigParamService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

@WebListener
public class ConstantsServletContextListener implements ServletContextListener {
    /**
     *当服务启动时，读取BasConfigParam中的所有数据，存入全局常量中
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("----------------服务启动-------------------");
        ServletContext context=servletContextEvent.getServletContext();
        ApplicationContext ctx= WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        ConfigParamService configParamService=(ConfigParamService)ctx.getBean("configParamService");
        List<BasConfigParam> list = configParamService.findAllConfig();
        for(int i=0;i<list.size();i++){
            BasConfigParam param=list.get(i);
            Global.CONFIG.put(param.getCode(),param.getValue());
            System.out.println("------->>>>>>>>>>>>>>"+param.getCode()+":::"+param.getValue());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("----------------服务终止-------------------");
    }
}
