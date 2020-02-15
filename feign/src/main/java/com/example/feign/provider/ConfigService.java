package com.example.feign.provider;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "config-client")
public interface ConfigService {

    @RequestMapping("/provider")
    String provider(@RequestParam("name") String name);
}
