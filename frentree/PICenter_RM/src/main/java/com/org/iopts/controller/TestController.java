package com.org.iopts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Test API", description = "테스트 API")
public class TestController {

    @GetMapping("/hello")
    @Operation(summary = "Hello World", description = "간단한 테스트 엔드포인트")
    public Map<String, Object> hello() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "PICenter_RM API is working!");
        response.put("version", "rm_2.0");
        return response;
    }

    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "서버 상태 확인")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("database", "MariaDB Connected");
        return response;
    }
}
