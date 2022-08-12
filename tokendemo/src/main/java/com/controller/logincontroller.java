package com.controller;

import com.domain.ResponseResult;
import com.domain.User;
import com.service.loginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName logincontroller
 * @Description TODO
 * @Date 2022/8/11 18:42
 */
@RestController
public class logincontroller {
    @Autowired
    private loginService loginService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
       return loginService.login(user);
    }
    @GetMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }

}
