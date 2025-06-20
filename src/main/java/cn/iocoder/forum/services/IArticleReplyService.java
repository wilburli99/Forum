package cn.iocoder.forum.services;

import cn.iocoder.forum.model.ArticleReply;
import org.springframework.transaction.annotation.Transactional;

public interface IArticleReplyService {

    /**
     * 创建文章回复
     * @param articleReply 文章回复
     */
    @Transactional
    void create(ArticleReply articleReply);
}
