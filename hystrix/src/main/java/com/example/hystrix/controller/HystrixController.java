package com.example.hystrix.controller;

import com.example.hystrix.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HystrixController {

    @Autowired
    private ConfigService configService;

    @RequestMapping("/test")
    public String testHystrixFellback(){
        return configService.testError("test");
    }
}
