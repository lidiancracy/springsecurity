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
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName loginserviceimpl
 * @Description TODO
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
        redisCache.setCacheObject("login2"+userid,loginUser);
        return new ResponseResult(200,"登录成功",map);

    }
}
