package cn.iocoder.forum.services;

import cn.iocoder.forum.model.Message;

public interface IMessageService {

    /**
     * 发送站内信息
     * @param message 站内信息对象
     */
    void create(Message message);
}
