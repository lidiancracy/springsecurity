package com.service.impl;

import com.domain.LoginUser;
import com.domain.ResponseResult;
import com.domain.User;
import com.service.loginService;
import com.utils.JwtUtil;
import com.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName loginserviceimpl
 * @Description 返回的token其实是 userid 的jwt加密
 * @Date 2022/8/11 18:52
 */
@Service
public class loginserviceimpl implements loginService {
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
//        进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = manager.authenticate(authenticationToken);
//        认证没通过 提示
        if(authenticate==null){
            throw  new RuntimeException("登录失败"); }
        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        //把完整的用户信息存入redis  userid作为key
        redisCache.setCacheObject("login"+userid,loginUser);
        return new ResponseResult(200,"登录成功",map);

    }

    @Override
    public ResponseResult logout() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        //删除redis中的值
        redisCache.deleteObject("login"+userid);
        return new ResponseResult(200,"注销成功");
    }
}
