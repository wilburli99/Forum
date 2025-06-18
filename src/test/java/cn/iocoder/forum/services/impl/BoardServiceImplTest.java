package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.model.Board;
import cn.iocoder.forum.services.IBoardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BoardServiceImplTest {
    @Resource
    private IBoardService boardService;
    @Resource
    private ObjectMapper objectMapper;

    @Test
    void selectByNum() {
        List<Board> result = boardService.selectByNum(1);
        System.out.println(result);
    }


    @Test
    @Transactional
    void addOneArticleCountById() {
        boardService.addOneArticleCountById(1L);
        System.out.println("更新成功");
    }

    @Test
    void selectById() throws JsonProcessingException {
        Board board = boardService.selectById(11L);
        System.out.println(objectMapper.writeValueAsString(board));
    }
}