package cn.iocoder.forum.dao;

import cn.iocoder.forum.model.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {
    int insert(Article row);

    int insertSelective(Article row);

    Article selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Article row);

    int updateByPrimaryKeyWithBLOBs(Article row);

    int updateByPrimaryKey(Article row);

    /**
     * 查询帖子列表
     * @return
     */
    List<Article> selectAll();

    /**
     * 根据版块ID查询帖子列表
     * @param boardId
     * @return
     */
    List<Article> selectAllByBoardId(@Param("boardId") Long boardId);

    /**
     * 根据ID查询帖子
     * @param id
     * @return
     */
    Article selectDetailById(@Param("id") Long id);

    /**
     * 根据ID查询帖子
     * @param userId 用户ID
     * @return
     */
    List<Article> selectByUserId(@Param("userId") Long userId);
}