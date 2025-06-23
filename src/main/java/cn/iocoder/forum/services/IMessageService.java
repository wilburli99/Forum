package cn.iocoder.forum.services;

import cn.iocoder.forum.model.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IMessageService {

    /**
     * 发送站内信息
     * @param message 站内信息对象
     */
    void create(Message message);

    /**
     * 根据用户ID查询未读的站内信数量
     * @param receiveUserId 接收用户ID
     * @return 未读的站内信数量
     */
    Integer selectUnreadCount(Long receiveUserId);

    /**
     * 根据用户ID查询所有站内信
     * @param receiveUserId 接收用户ID
     * @return List<Message> 站内信列表
     */
    List<Message> selectByReceiveUserId(Long receiveUserId);

    /**
     * 根据ID更新站内信状态
     * @param id 站内信ID
     * @param state 状态
     */
    void updateStateById(Long id, Byte state);

    /**
     * 根据ID查询站内信
     * @param id 站内信ID
     * @return Message
     */
    Message selectById(Long id);

    /**
     * 回复站内信
     * @param replyId 回复ID
     * @param message 站内信
     */
    @Transactional
    void reply(Long replyId, Message message);
}
