package com.base.wang.filter;

import com.base.wang.config.AccountShiroUtil;
import com.base.wang.entity.BasTest;
import com.base.wang.service.JedisClient;
import com.base.wang.util.JsonUtil;
import com.base.wang.util.JwtUtil;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class LoginFilter extends AccessControlFilter {
    @Autowired
    private JedisClient jedisClient;

    //表示是否允许访问；mappedValue就是[urls]配置中拦截器参数部分，如果允许访问返回true，否则false；
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    //表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可。
    @Override
    protected boolean onAccessDenied(ServletRequest req, ServletResponse resp) throws Exception {
        BasTest basTest= AccountShiroUtil.getCurrentUser();
        if(basTest!=null){
            return true;
        }else {
            HttpServletRequest request = (HttpServletRequest) req;
            Cookie[] cookies = request.getCookies();
            if (cookies == null || cookies.length <= 0) {
                return true;
            }else {
                return tokenJudge(cookies);
            }
        }
    }
    private boolean tokenJudge(Cookie[] cookies) throws Exception {
        for (Cookie cookie : cookies) {
            String cookieName = cookie.getName();
            if (!cookieName.equals("token")) continue;
            String token = cookie.getValue();
            String userInfo = JwtUtil.verify(token);
            Map map = JsonUtil.json2Map(userInfo);
            String username = map.get("username").toString();
            String password = map.get("password").toString();
            BasTest basTest = new BasTest();
            basTest.setUsername(username);
            basTest.setPassword(password);
            AccountShiroUtil.setUser(basTest);
            return false;
        }
        System.out.println("------------------------------");
        return true;
    }
}
