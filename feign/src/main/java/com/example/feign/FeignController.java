package com.example.feign;

import com.example.feign.provider.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignController {

    @Autowired
    ConfigService configService;

    @RequestMapping("/feign")
    public String getConfigProvider(){
        return configService.provider("joeson");
    }
}
