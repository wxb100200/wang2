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
import java.security.NoSuchAlgorithmException;
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
    public PageReturn login(String username,String password,HttpServletRequest request,HttpServletResponse response) throws NoSuchAlgorithmException {
        // 1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        // 2.封装用户数据
        UsernamePasswordToken passwordToken= new UsernamePasswordToken(username, password);
        // 3.执行登录方法
        try{
            subject.login(passwordToken);
            Date date= DateUtils.addHours(new Date(),1);
            Map<String,String> mapData=new HashMap<String, String>();
            mapData.put("username",username);
            mapData.put("password",password);
            String token= JwtUtil.generateToken(JsonUtil.toJson(mapData),date);
            addCookie(request,response,token);
            return PageReturn.successData(token);
        } catch (UnknownAccountException e){
            e.printStackTrace();
            return PageReturn.fail("用户名不存在!");
        } catch (IncorrectCredentialsException e){
            return PageReturn.fail("密码错误!");
        }
    }

    private void addCookie(HttpServletRequest request,HttpServletResponse response,String token){
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            String cookieName=cookie.getName();
            if(cookieName.equals("token")){
                cookie.setValue(null);
                cookie.setMaxAge(0);// 立即销毁cookie
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(30 * 60);// 设置为30min
        cookie.setPath("/");
        cookie.setDomain("huzldt.com");
        response.addCookie(cookie);
    }
}
