package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.model.Message;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MessageServiceImplTest {
    @Resource
    private MessageServiceImpl messageService;

    @Test
    void create() {
        Message message = new Message();
        message.setPostUserId(3L);
        message.setReceiveUserId(2L);
        message.setContent("单元测试");

        messageService.create(message);
        System.out.println("发送成功");
    }
}