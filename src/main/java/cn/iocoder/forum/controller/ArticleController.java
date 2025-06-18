package cn.iocoder.forum.controller;

import cn.iocoder.forum.common.AppResult;
import cn.iocoder.forum.common.ResultCode;
import cn.iocoder.forum.config.AppConfig;
import cn.iocoder.forum.model.Article;
import cn.iocoder.forum.model.Board;
import cn.iocoder.forum.model.User;
import cn.iocoder.forum.services.impl.ArticleServiceImpl;
import cn.iocoder.forum.services.impl.BoardServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Tag(name = "帖子接口", description = "帖子接口")
@RequestMapping("/article")
@RestController
public class ArticleController {
    @Resource
    private BoardServiceImpl boardService;
    @Resource
    private ArticleServiceImpl articleService;

    @Operation(summary = "创建帖子", description = "创建帖子")
    @PostMapping("/create")
    public AppResult create(HttpServletRequest  request,
                            @Param("版块ID") @RequestParam("boardId") @NotNull Long boardId,
                            @Param("标题") @RequestParam("title") @NotNull String title,
                            @Param("内容") @RequestParam("content") @NotNull String content) {
        // 检验用户是否禁言
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute(AppConfig.USER_SESSION);
        if (user.getState() == 1) {
            // 用户已禁言
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        // 板块校验
        Board board = boardService.selectById(boardId.longValue());
        if (board == null || board.getDeleteState() == 1 || board.getState() == 1) {
            // 打印日志
            log.warn(ResultCode.FAILED_BOARD_BANNED.toString());
            // 返回响应
            return AppResult.failed(ResultCode.FAILED_BOARD_BANNED);
        }
        // 封装文章对象
        Article article = new Article();
        article.setUserId(user.getId());
        article.setBoardId(boardId);
        article.setTitle(title);
        article.setContent(content);
        // 调用service
        articleService.create(article);
        // 返回结果
        return AppResult.success();
    }

    @Operation(summary = "根据帖子Id获取详情", description = "根据帖子Id获取详情")
    @GetMapping("getAllByBoardId")
    public AppResult<List> getAllByBoardId(@Param("板块ID") @RequestParam(value = "boardId", required = false) Long boardId) {
        List<Article> articles;
        // 根据board ID是否为空来决定是首页的帖子列表还是其他板块的帖子列表
        if (boardId == null) {
            articles = articleService.selectAll();
        } else {
            articles = articleService.selectAllByBoardId(boardId);
        }

        if (articles == null) {
            articles = new ArrayList<>();
        }
        // 返回响应结果
        return AppResult.success(articles);
    }

    @Operation(summary = "帖子详情", description = "帖子详情")
    @GetMapping("/details")
    public AppResult<Article> getDetails(@Param("id") @RequestParam("id") @NotNull Long id) {
        // 调用service，获取帖子详情
        Article article = articleService.selectDetailById(id);
        // 判断帖子对象是否为空
        if (article == null) {
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }
        return AppResult.success(article);
    }
}
