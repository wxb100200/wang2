package com.base.wang.controller;

import com.base.wang.common.PageReturn;
import com.base.wang.service.JedisClient;
import com.base.wang.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private JedisClient jedisClient;

    @RequestMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        return "redirect:/login.html";
    }

    @ResponseBody
    @RequestMapping("/login")
    public PageReturn login(String username, String password,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Cookie uuidCookie=findCookie(request,"uuid");
        if (uuidCookie==null) {
            return userNameLogin(request,response, username,  password);
        }else {
            return uuidLogin(response, uuidCookie);
        }
    }

    private PageReturn uuidLogin( HttpServletResponse response, Cookie uuidCookie) throws Exception {
        String uuid=uuidCookie.getValue();
        String redisValue=jedisClient.hget("wangxiaobing",uuid);
        String userInfo=JwtUtil.verify(redisValue);
        Map map=JsonUtil.json2Map(userInfo);
        String username=map.get("username").toString();
        String password=map.get("password").toString();
        // 1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        // 2.封装用户数据
        UsernamePasswordToken passwordToken= new UsernamePasswordToken(username, password);
        // 3.执行登录方法
        try{
            subject.login(passwordToken);
            Map<String,Object> result=new HashMap<String, Object>();
            result.put("mobile",username);
            result.put("uuid",uuid);
            return PageReturn.successData(result);
        } catch (UnknownAccountException e){
            e.printStackTrace();
            cleanCookie(response,uuidCookie);
            return PageReturn.fail("用户名不存在!");
        } catch (IncorrectCredentialsException e){
            cleanCookie(response,uuidCookie);
            return PageReturn.fail("密码错误!");
        }
    }

    private  PageReturn userNameLogin(HttpServletRequest request,HttpServletResponse response,String username, String password){
        // 1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        // 2.封装用户数据
        UsernamePasswordToken passwordToken= new UsernamePasswordToken(username, password);
        // 3.执行登录方法
        try{
            subject.login(passwordToken);
            String uuid = UUIDUtil.generateNumber();
            Date date= DateUtils.addHours(new Date(),1);
            Map<String,String> data=new HashMap<String, String>();
            data.put("username",username);
            data.put("password",password);
            String token= JwtUtil.generateToken(JsonUtil.toJson(data),date);
            jedisClient.hset("wangxiaobing",uuid,token);
            Map<String,Object> result=new HashMap<String, Object>();
            result.put("mobile",username);
            result.put("uuid",uuid);
            addCookie(response,uuid);
            return PageReturn.successData(result);
        } catch (UnknownAccountException e){
            e.printStackTrace();
            return PageReturn.fail("用户名不存在!");
        } catch (IncorrectCredentialsException e){
            return PageReturn.fail("密码错误!");
        }

    }
    private Cookie findCookie(HttpServletRequest request,String name){
        Cookie[] cookies = request.getCookies();
        if(cookies==null || cookies.length<=0)return null;
        for(Cookie cookie:cookies){
            String cookieName=cookie.getName();
            if(cookieName.equals(name))return cookie;
        }
        return null;
    }
    private void cleanCookie(HttpServletResponse response,Cookie cookie){
        cookie.setValue(null);
        cookie.setMaxAge(0);// 立即销毁cookie
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    private void addCookie(HttpServletResponse response,String uuid){
        Cookie cookie = new Cookie("uuid", uuid);
        cookie.setMaxAge(30 * 60);// 设置为30min
        cookie.setPath("/");
        cookie.setDomain("huzldt.com");
        response.addCookie(cookie);
    }
}
