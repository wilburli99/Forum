package cn.iocoder.forum.services;

import cn.iocoder.forum.model.Board;

import java.util.List;

public interface IBoardService {
    // 查询板块
    List<Board> selectByNum(Integer num);
}
