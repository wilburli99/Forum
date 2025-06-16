package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.model.User;
import cn.iocoder.forum.services.IUserService;
import cn.iocoder.forum.utils.MD5Util;
import cn.iocoder.forum.utils.UUIDUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceImplTest {
    @Resource
    private IUserService userService;

    @Test
    void createNormalUser() {
        // 构造user对象
        User user = new User();
        user.setUsername("bitboy");
        user.setNickname("bitboy");

        // 原始密码
        user.setPassword("123456");
        // 生成盐
        String salt = UUIDUtil.uuid_32();
        // 生成密码的密文
        String md5Password = MD5Util.md5Salt(user.getPassword(), salt);
        // 设置加密后的密码
        user.setPassword(md5Password);
        // 设置盐
        user.setSalt(salt);

        // 调用services层的方法
        userService.createNormalUser(user);
        // 打印结果
        System.out.println(user);
    }
}