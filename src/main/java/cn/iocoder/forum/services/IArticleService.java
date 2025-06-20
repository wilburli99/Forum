package cn.iocoder.forum.services;

import cn.iocoder.forum.model.Article;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 根据帖子ID查询帖子详情
     * @param id 帖子ID
     * @return
     */
    Article selectDetailById(Long id);

    /**
     * 根据帖子ID查询帖子
     * @param id 帖子ID
     * @return
     */
    Article selectById(Long id);

    /**
     * 编辑帖子
     * @param id 帖子ID
     * @param title 帖子标题
     * @param content 帖子内容
     */
    public void modify(Long id, String title, String content);
}
