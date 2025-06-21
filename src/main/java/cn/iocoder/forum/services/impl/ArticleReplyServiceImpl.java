package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.common.AppResult;
import cn.iocoder.forum.common.ResultCode;
import cn.iocoder.forum.dao.ArticleReplyMapper;
import cn.iocoder.forum.exception.ApplicationException;
import cn.iocoder.forum.model.ArticleReply;
import cn.iocoder.forum.services.IArticleReplyService;
import cn.iocoder.forum.services.IArticleService;
import cn.iocoder.forum.utils.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ArticleReplyServiceImpl implements IArticleReplyService {

    @Resource
    private ArticleReplyMapper articleReplyMapper;
    @Resource
    private IArticleService articleService;
    @Override
    public void create(ArticleReply articleReply) {
        // 非空校验
        if (articleReply == null || articleReply.getArticleId() == null
                || articleReply.getPostUserId() == null
                || StringUtil.isEmpty(articleReply.getContent())) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 设置默认值
        articleReply.setReplyId(null);
        articleReply.setReplyUserId(null);
        articleReply.setLikeCount(0);
        articleReply.setState((byte) 0);
        articleReply.setDeleteState((byte) 0);
        Date date = new Date();
        articleReply.setCreateTime(date);
        articleReply.setUpdateTime(date);
        // 写入数据库
        int row = articleReplyMapper.insertSelective(articleReply);
        if (row != 1) {
            log.info(ResultCode.ERROR_SERVICES.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
        // 更新帖子的回复数
        articleService.addOneReplyCountById(articleReply.getArticleId());
        // 打印成功日志
        log.info("回复成功, article id = " + articleReply.getArticleId() + ", user id = " +
                articleReply.getPostUserId());
    }

    @Override
    public List<ArticleReply> selectByArticleId(Long articleId) {
        // 非空校验
        if (articleId == null || articleId <= 0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 调用DAO
        List<ArticleReply> result = articleReplyMapper.selectByArticleId(articleId);
        return result;
    }
}
