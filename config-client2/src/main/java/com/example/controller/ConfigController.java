package com.example.controller;

import com.example.config.GitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigController {

    @Autowired
    private GitConfig gitConfig;

    @RequestMapping("/log")
    public Object log(){
        return gitConfig;
    }

    @RequestMapping("/provider")
    public String ConfigProvider(@RequestParam String name){
        return String.format("Hi %s,this is the second Config Provider.", name);
    }
}
