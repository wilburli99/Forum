package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.common.AppResult;
import cn.iocoder.forum.common.ResultCode;
import cn.iocoder.forum.dao.ArticleMapper;
import cn.iocoder.forum.exception.ApplicationException;
import cn.iocoder.forum.model.Article;
import cn.iocoder.forum.model.Board;
import cn.iocoder.forum.model.User;
import cn.iocoder.forum.services.IArticleService;
import cn.iocoder.forum.services.IBoardService;
import cn.iocoder.forum.services.IUserService;
import cn.iocoder.forum.utils.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ArticleServiceImpl implements IArticleService {

    @Resource
    private ArticleMapper articleMapper;
    // 用户和版块的操作
    @Resource
    private IUserService userService;
    @Resource
    private IBoardService boardService;
    @Override
    public void create(Article article) {
        // 非空校验
        if (article == null || article.getUserId() == null || article.getBoardId() == null
                || StringUtil.isEmpty(article.getTitle())
                || StringUtil.isEmpty(article.getContent())) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        Date date = new Date();
        // 设置默认值
        article.setVisitCount(0);
        article.setReplyCount(0);
        article.setLikeCount(0);
        article.setDeleteState((byte) 0);
        article.setState((byte) 0);
        article.setCreateTime(date);
        article.setUpdateTime(date);
        // 写入数据库
        int articleRow = articleMapper.insertSelective(article);
        if (articleRow <= 0) {
            log.warn(ResultCode.FAILED_CREATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }

        // 获取用户信息
        User user = userService.selectById(article.getUserId());
        // 没有找到指定的用户信息
        if (user == null) {
            log.warn(ResultCode.FAILED_CREATE.toString() + ", 发贴失败, user id = " + article.getUserId());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        // 更新用户发帖数
        userService.addOneArticleCountById(article.getUserId());

        // 获取板块信息
        Board board = boardService.selectById(article.getBoardId());
        // 是否在数据库在有对应的版块
        if (board == null) {
            log.warn(ResultCode.FAILED_CREATE.toString() + ", 发贴失败, board id = " + article.getBoardId());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        // 更新版块中的帖子数量
        boardService.addOneArticleCountById(article.getBoardId());

        // 打印日志
        log.info(ResultCode.SUCCESS.toString() + ", user id = " + article.getUserId()
                + ", board id = " + article.getBoardId() + ", article id = "+article.getId() + "发帖成功");
    }

    @Override
    public List<Article> selectAll() {
        List<Article> articles = articleMapper.selectAll();
        return articles;
    }

    @Override
    public List<Article> selectAllByBoardId(Long boardId) {
        // 非空校验
        if (boardId == null || boardId <= 0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 板块校验
        Board board = boardService.selectById(boardId);
        if (board == null) {
            log.warn(ResultCode.FAILED_BOARD_NOT_EXISTS.toString() + ", board id = " + boardId);
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }
        // 调用DAO来创建查询
        List<Article> articles = articleMapper.selectAllByBoardId(boardId);
        // 返回结果
        return articles;
    }

    @Override
    public Article selectDetailById(Long id) {
        // 非空校验
        if (id == null || id <= 0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 调用DAO来创建查询
        Article article = articleMapper.selectDetailById(id);
        // 判断结果是否为空
        if (article == null) {
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS));
        }
        // 更新帖子的访问数
        Article updateArticle = new Article();
        updateArticle.setId(article.getId());
        updateArticle.setVisitCount(article.getVisitCount() + 1);
        // 保存到数据库
        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if (row != 1) {
            log.warn(ResultCode.ERROR_SERVICES.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
        // 更新返回对象的访问数
        article.setVisitCount(article.getVisitCount() + 1);
        // 返回帖子详情
        return article;
    }

    @Override
    public Article selectById(Long id) {
        // 参数校验
        if (id == null || id <= 0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 调用DAO查询
        Article article = articleMapper.selectByPrimaryKey(id);
        // 返回结果
        return article;
    }

    @Override
    public void modify(Long id, String title, String content) {
        // 参数校验
        if (id == null || StringUtil.isEmpty(title) || StringUtil.isEmpty(content)) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 构建要更新的帖子
        Article updateArticle = new Article();
        updateArticle.setId(id); // 帖子ID
        updateArticle.setTitle(title); // 帖子标题
        updateArticle.setContent(content); // 帖子内容
        updateArticle.setUpdateTime(new Date()); // 更新时间
        // 调用DAO，执行更新
        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        if (row != 1) {
            log.warn(ResultCode.ERROR_SERVICES.toString() + "受影响的行数不等于1" + row);
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
    }
}
