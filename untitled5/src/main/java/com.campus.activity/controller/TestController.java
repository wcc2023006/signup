package com.campus.activity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "校园活动系统运行正常");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}