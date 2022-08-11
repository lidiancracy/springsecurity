package com;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @ClassName serv
 * @Description TODO
 * @Date 2022/8/11 15:25
 */
@SpringBootTest
public class serv {


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
