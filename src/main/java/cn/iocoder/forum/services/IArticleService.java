package cn.iocoder.forum.services;

import cn.iocoder.forum.model.Article;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IArticleService {
    /**
     * 发布帖子
     * @param article 要发布的帖子
     */
    @Transactional // 当前方法中的执行过程会被事务管理起来
    void create (Article article);

    /**
     * 查询所有帖子
     * @return
     */
    List<Article> selectAll();

    /**
     * 根据版块ID查询所有帖子
     * @param boardId 版块ID
     * @return
     */
    List<Article> selectAllByBoardId(Long boardId);
}
