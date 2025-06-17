package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.model.User;
import cn.iocoder.forum.services.IUserService;
import cn.iocoder.forum.utils.MD5Util;
import cn.iocoder.forum.utils.UUIDUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceImplTest {
    @Resource
    private IUserService userService;
    @Transactional // 测试时，开启事务，在测试完成后回滚，不会污染数据库
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
    @Transactional
    @Test
    void selectByUserName() {
        User user = userService.selectByUserName("bitboy");
        System.out.println(user);
    }
    @Transactional
    @Test
    void login() {
        User user = userService.login("bitboy ", "123456");
        System.out.println(user);
    }
    @Transactional
    @Test
    void selectById() {
        User user = userService.selectById(3L);
        System.out.println(user);
    }
}