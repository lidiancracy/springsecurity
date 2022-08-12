package com.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.domain.LoginUser;
import com.domain.User;
import com.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * user detail service是安全框架希望我们实现的接口
 * 这个是我们自定义的，我们希望调用数据库来验证账户密码，需要自定义
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    /**
     * @Author lidian
     * @Date 2022/8/11
     * @Description 根据用户名查user实体
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        if(user==null){
            throw new RuntimeException("用户不存在");
        }
//        将用户 封装成userdetails对象,userdetail是接口,我们返回它的实现类对象
        List<String> strings = new ArrayList<>();
        strings.add("admin");
        strings.add("test");
        return new LoginUser(user,strings);
    }
}
