package com.example.controller;

import com.example.bean.User;
import com.example.config.GitConfig;
import com.example.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RefreshScope
public class ConfigController {

    @Autowired
    private GitConfig gitConfig;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/log")
    public Object log(){
        return gitConfig;
    }

    @RequestMapping("/provider")
    public String ConfigProvider(@RequestParam String name){
        return String.format("Hi %s,this is the frist Config Provider.", name);
    }

    @RequestMapping("/query")
    public List<User> query(){
        return userMapper.query(new User());
    }
}
