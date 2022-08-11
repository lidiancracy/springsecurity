package com.service;

import com.domain.ResponseResult;
import com.domain.User;

/**
 * @ClassName loginService
 * @Description TODO
 * @Date 2022/8/11 18:49
 */
public interface loginService {

    ResponseResult login(User user);
}
