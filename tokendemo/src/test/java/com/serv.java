package com;

import com.domain.User;
import com.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

/**
 * @ClassName serv
 * @Description TODO
 * @Date 2022/8/11 15:25
 */
@SpringBootTest
public class serv {
    @Autowired
    private UserService userService;
    @Test
    public void test1(){
        List<User> list = userService.list();
        System.out.println(list);
    }

    /**
     * @Author lidian
     * @Date 2022/8/11
     * @Description 每次盐都是随机生成的, 两段乱码都对应同一个原始密码
     */
    @Test
    public void test2(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123456");
        String encode2 = bCryptPasswordEncoder.encode("123456");
        System.out.println(encode);
        System.out.println(encode2);
        System.out.println(bCryptPasswordEncoder.matches("123456", encode));
        System.out.println(bCryptPasswordEncoder.matches("123456", encode2));

    }
}
