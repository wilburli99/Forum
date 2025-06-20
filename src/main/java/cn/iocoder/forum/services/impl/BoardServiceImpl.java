package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.common.AppResult;
import cn.iocoder.forum.common.ResultCode;
import cn.iocoder.forum.dao.BoardMapper;
import cn.iocoder.forum.exception.ApplicationException;
import cn.iocoder.forum.model.Board;
import cn.iocoder.forum.services.IBoardService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class BoardServiceImpl implements IBoardService {
    @Resource
    private BoardMapper boardMapper;
    @Override
    public List<Board> selectByNum(Integer num) {
        // 非空校验
        if (num <= 0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 调用DAO中的数据
        List<Board> result = boardMapper.selectByNum(num);
        // 返回结果
        return result;
    }

    @Override
    public Board selectById(Long id) {
        if (id == null || id <= 0) {
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }
        Board board = boardMapper.selectByPrimaryKey(id);
        return board;
    }

    @Override
    public void addOneArticleCountById(Long id) {
        if (id == null || id <= 0) {
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }
        // 查询对应板块
        Board board = boardMapper.selectByPrimaryKey(id);
        if (board == null) {
            log.warn(ResultCode.ERROR_IS_NULL.toString() + "boardId = " + id);
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 更新帖子数量，需要重新创建对象
        Board updateBoard = new Board();
        updateBoard.setId(id);
        updateBoard.setArticleCount(board.getArticleCount() + 1);
        // 通过调用动态sql语句，更新板块帖子数量
        int row = boardMapper.updateByPrimaryKeySelective(updateBoard);
        // 判断受影响的行数
        if (row != 1) {
            log.warn(ResultCode.FAILED.toString() + "受影响的行数不等于1" + row);
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }

    @Override
    public void subOneArticleCountById(Long id) {
        if (id == null || id <= 0) {
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }
        // 查询对应板块
        Board board = boardMapper.selectByPrimaryKey(id);
        if (board == null) {
            log.warn(ResultCode.ERROR_IS_NULL.toString() + "board Id = " + id);
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 更新帖子数量，需要重新创建对象
        Board updateBoard = new Board();
        updateBoard.setId(id);
        updateBoard.setArticleCount(board.getArticleCount() - 1);
        // 判断-1之后是否小于0
        if (updateBoard.getArticleCount() < 0) {
            // 如果小于0，则设置为0
            updateBoard.setArticleCount(0);
        }
        // 通过调用动态sql语句，更新板块帖子数量
        int row = boardMapper.updateByPrimaryKeySelective(updateBoard);
        // 判断受影响的行数
        if (row != 1) {
            log.warn(ResultCode.FAILED.toString() + "受影响的行数不等于1" + row);
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }
}
