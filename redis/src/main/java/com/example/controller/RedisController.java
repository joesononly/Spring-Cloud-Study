package com.example.controller;

import com.example.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    RedisUtil redisUtil;

    @RequestMapping("/{key}")
    public String get(@PathVariable(name = "key") String key){
        return (String) redisUtil.getValue(key);
    }

}
