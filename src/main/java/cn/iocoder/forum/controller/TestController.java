package cn.iocoder.forum.controller;

import cn.iocoder.forum.exception.ApplicationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
    @RequestMapping("/exception")
    public String testException() throws Exception {
        throw new Exception("测试异常");
    }
    @RequestMapping("/appException")
    public String testApplcationException() {
        throw new ApplicationException("这是一个自定义的异常");
    }
}
