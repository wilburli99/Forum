package cn.iocoder.forum.controller;

import cn.iocoder.forum.common.AppResult;
import cn.iocoder.forum.common.ResultCode;
import cn.iocoder.forum.config.AppConfig;
import cn.iocoder.forum.model.Article;
import cn.iocoder.forum.model.ArticleReply;
import cn.iocoder.forum.model.User;
import cn.iocoder.forum.services.IArticleReplyService;
import cn.iocoder.forum.services.IArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 帖子回复接口
 */
@Tag(name = "帖子回复接口", description = "用于帖子回复的接口")
@Slf4j
@RestController
@RequestMapping("/reply")
public class ArticleReplyController {
    @Resource
    private IArticleReplyService articleReplyService;
    @Resource
    private IArticleService articleService;

    @Operation(summary = "创建帖子回复", description = "创建帖子回复")
    @PostMapping("/create")
    public AppResult create(HttpServletRequest request,
                            @Param("帖子ID") @RequestParam("articleId") @NotNull Long articleId,
                            @Param("帖子内容") @RequestParam("content") @NotNull String  content) {
        // 获取用户信息
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 判断用户是否已被禁言
        if (user.getState() == 1) {
            // 表示已禁言
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        // 获取要回复的帖子
        Article article = articleService.selectById(articleId);
        // 是否存在，或已删除
        if (article == null || article.getDeleteState() == 1) {
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }
        // 是否封帖
        if (article.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_ARTICLE_BANNED);
        }
        // 构建回复对象
        ArticleReply articleReply = new ArticleReply();
        articleReply.setArticleId(articleId); // 要回复的帖Id
        articleReply.setPostUserId(user.getId()); // 回复的发送者
        articleReply.setContent(content); // 回复的内容
        // 写入回复
        articleReplyService.create(articleReply);
        return AppResult.success();
    }
}
