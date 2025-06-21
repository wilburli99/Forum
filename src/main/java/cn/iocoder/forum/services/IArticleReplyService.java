package cn.iocoder.forum.services;

import cn.iocoder.forum.model.ArticleReply;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IArticleReplyService {

    /**
     * 创建文章回复
     * @param articleReply 文章回复
     */
    @Transactional
    void create(ArticleReply articleReply);

    /**
     * 根据帖子Id查询帖子回复
     * @param articleId 帖子Id
     * @return
     */
    List<ArticleReply> selectByArticleId(Long articleId);
}
