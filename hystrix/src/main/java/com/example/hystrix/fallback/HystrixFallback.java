package com.example.hystrix.fallback;

import com.example.hystrix.service.ConfigService;
import org.springframework.stereotype.Component;

@Component
public class HystrixFallback implements ConfigService {

    @Override
    public String testError(String error) {
        return "this is a fallback Method.";
    }
}
