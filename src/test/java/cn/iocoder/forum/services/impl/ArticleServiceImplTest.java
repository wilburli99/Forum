package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.model.Article;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ArticleServiceImplTest {
    @Resource
    private ArticleServiceImpl articleService;

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
}