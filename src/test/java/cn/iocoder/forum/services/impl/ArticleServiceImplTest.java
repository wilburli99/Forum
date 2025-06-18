package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.model.Article;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ArticleServiceImplTest {
    @Resource
    private ArticleServiceImpl articleService;
    @Resource
    private ObjectMapper objectMapper;

    @Test
    void create() {
        Article article = new Article();
        article.setUserId(2L);
        article.setBoardId(1L);
        article.setTitle("单元测试");
        article.setContent("单元测试");
        articleService.create(article);
        System.out.println("发帖成功");
    }

    @Test
    void selectAll() throws JsonProcessingException {
        List<Article> articles = articleService.selectAll();
        System.out.println(objectMapper.writeValueAsString(articles));
    }

    @Test
    void selectAllByBoardId() throws JsonProcessingException {
        List<Article> articles = articleService.selectAllByBoardId(1L);
        System.out.println(objectMapper.writeValueAsString(articles));
    }
}