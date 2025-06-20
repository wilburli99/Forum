package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.model.ArticleReply;
import cn.iocoder.forum.services.IArticleReplyService;
import cn.iocoder.forum.services.IArticleService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ArticleReplyServiceImplTest {
    @Resource
    private IArticleReplyService articleReplyService;

    @Test
    void create() {
        ArticleReply articleReply = new ArticleReply();
        articleReply.setArticleId(2L);
        articleReply.setPostUserId(2L);
        articleReply.setContent("单元测试回复");
        articleReplyService.create(articleReply);
        System.out.println("单元测试回复成功");
    }
}