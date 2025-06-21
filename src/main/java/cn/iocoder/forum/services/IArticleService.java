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
     * 根据ID查询帖子
     * @param userId 用户ID
     * @return
     */
    List<Article> selectByUserId(Long userId);

    /**
     * 编辑帖子
     * @param id 帖子ID
     * @param title 帖子标题
     * @param content 帖子内容
     */
    void modify(Long id, String title, String content);

    /**
     * 帖子点赞
     * @param id 帖子ID
     */
    void thumbsUpById(Long id);

    /**
     * 删除帖子
     * @param id 帖子ID
     */
    @Transactional
    void deleteById(Long id);

    /**
     * 帖子的回复数量 +1
     * @param id 版块Id
     */
    void addOneReplyCountById(Long id);
}
