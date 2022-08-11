package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName TokenApp
 * @Description TODO
 * @Date 2022/8/11 14:56
 */
@SpringBootApplication
@MapperScan("com.mapper")
public class TokenApp {
    public static void main(String[] args) {
        SpringApplication.run(TokenApp.class,args);
    }
}
