package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.model.Board;
import cn.iocoder.forum.services.IBoardService;
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
}