package cn.iocoder.forum.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

/**
 * 测试Controller - 用于验证Swagger是否正常工作
 */
@Tag(name = "测试接口", description = "用于测试Swagger功能的接口")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Operation(summary = "Hello World", description = "返回Hello World消息")
    @GetMapping("/hello")
    public String hello() {
        return "Hello World from Forum API!";
    }

    @Operation(summary = "获取系统信息", description = "返回系统基本信息")
    @GetMapping("/info")
    public String getSystemInfo() {
        return "Forum System - Spring Boot 3.x + SpringDoc OpenAPI 3";
    }
    @Operation(summary = "通过用户名获取用户信息", description = "返回用户信息")
    @PostMapping("/hello/name")
    public String helloByName(@Param(value = "用户名") @RequestParam String name) {
        return "Hello " + name + " from Forum API!";
    }
}
