package com.example.hystrix.service;

import com.example.hystrix.fallback.HystrixFallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "config-client",fallback = HystrixFallback.class)
public interface ConfigService {

    @RequestMapping("/fallback")
    String testError(@RequestParam("error") String error);
}
