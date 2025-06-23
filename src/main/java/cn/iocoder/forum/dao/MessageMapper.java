package cn.iocoder.forum.dao;

import cn.iocoder.forum.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MessageMapper {
    int insert(Message row);

    int insertSelective(Message row);

    Message selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Message row);

    int updateByPrimaryKey(Message row);

    /**
     * 根据用户ID查询未读的站内信数量
     * @param receiveUserId 接收用户ID
     * @return 未读的站内信数量
     */
    Integer selectUnreadCount(@Param("receiveUserId") Long receiveUserId);
}