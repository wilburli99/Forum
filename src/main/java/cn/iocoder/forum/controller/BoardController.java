package cn.iocoder.forum.controller;

import cn.iocoder.forum.common.AppResult;
import cn.iocoder.forum.common.ResultCode;
import cn.iocoder.forum.exception.ApplicationException;
import cn.iocoder.forum.model.Board;
import cn.iocoder.forum.services.IBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Tag(name = "版块接口", description = "版块接口")
@RequestMapping("/board")
@RestController
public class BoardController {
    @Resource
    private IBoardService boardService;
    @Value("${forum-sys.index.board-num}")
    private Integer indexBoardNum;

    @Operation(summary = "版块列表", description = "版块列表")
    @GetMapping("/topList")
    public AppResult<List<Board>> topList() {
        log.info("首页板块个数" + indexBoardNum);
        // 调用Service查询结果
        List<Board> boards = boardService.selectByNum(indexBoardNum);
        // 判断是否为空
        if (boards == null) {
            boards = new ArrayList<>();
        }
        return AppResult.success(boards);
    }

    @Operation(summary = "根据板块ID获取板块信息", description = "根据板块ID获取板块信息")
    @GetMapping("/getById")
    public AppResult<Board> getById(@Param("板块ID") @RequestParam("id") @NotNull Long id) {
        // 调用Service查询结果
        Board board = boardService.selectById(id);
        // 校验结果
        if (board == null || board.getDeleteState() == 1) {
            log.warn(ResultCode.FAILED_BOARD_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }
        // 返回结果
        return AppResult.success(board);
    }
}
