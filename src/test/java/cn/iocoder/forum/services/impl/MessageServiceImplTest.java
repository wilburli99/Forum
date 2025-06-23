package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MessageServiceImplTest {
    @Resource
    private MessageServiceImpl messageService;
    @Resource
    private ObjectMapper objectMapper;

    @Test
    void create() {
        Message message = new Message();
        message.setPostUserId(3L);
        message.setReceiveUserId(2L);
        message.setContent("单元测试");

        messageService.create(message);
        System.out.println("发送成功");
    }

    @Test
    void selectUnreadCount() {
        Integer count = messageService.selectUnreadCount(2L);
        System.out.println("未读消息数：" + count);
        count = messageService.selectUnreadCount(3L);
        System.out.println("未读消息数：" + count);
    }

    @Test
    void selectByReceiveUserId() throws JsonProcessingException {
        List<Message> messages = messageService.selectByReceiveUserId(2L);
        System.out.println(objectMapper.writeValueAsString(messages));
        messages = messageService.selectByReceiveUserId(3L);
        System.out.println(objectMapper.writeValueAsString(messages));
        messages = messageService.selectByReceiveUserId(20L);
        System.out.println(objectMapper.writeValueAsString(messages));
    }

    @Test
    void updateStateById() {
        messageService.updateStateById(1L, (byte) 1);
        System.out.println("更新成功");
    }
}