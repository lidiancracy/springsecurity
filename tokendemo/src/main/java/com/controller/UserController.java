package com.controller;


import org.springframework.web.bind.annotation.*;

/**
 * 用户表(User)表控制层
 *
 * @author makejava
 * @since 2022-08-11 15:22:35
 */
@RestController
@RequestMapping("/")
public class UserController {
    @GetMapping("/")
   public String test1(){
       return "test";
   }
}

