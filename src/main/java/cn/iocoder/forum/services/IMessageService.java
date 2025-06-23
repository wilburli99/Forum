package cn.iocoder.forum.services;

import cn.iocoder.forum.model.Message;
import org.apache.ibatis.annotations.Param;

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
}
