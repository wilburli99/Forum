package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.common.AppResult;
import cn.iocoder.forum.common.ResultCode;
import cn.iocoder.forum.dao.MessageMapper;
import cn.iocoder.forum.exception.ApplicationException;
import cn.iocoder.forum.model.Message;
import cn.iocoder.forum.model.User;
import cn.iocoder.forum.services.IMessageService;
import cn.iocoder.forum.services.IUserService;
import cn.iocoder.forum.utils.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MessageServiceImpl implements IMessageService {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private IUserService userService;

    @Override
    public void create(Message message) {
        // 非空校验
        if (message == null || message.getPostUserId() == null || message.getReceiveUserId() == null
        || StringUtil.isEmpty(message.getContent())) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 校验接收者是否存在
        User user = userService.selectById(message.getReceiveUserId());
        if (user == null || user.getDeleteState() == 1) {
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 设置默认值
        message.setState((byte) 0); // 表示未读状态
        message.setDeleteState((byte) 0);
        // 设置创建与更新时间
        Date date = new Date();
        message.setCreateTime(date);
        message.setUpdateTime(date);
        // 调用DAO
        int row = messageMapper.insertSelective(message);
        if (row != 1) {
            log.warn(ResultCode.FAILED_CREATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
    }

    @Override
    public Integer selectUnreadCount(Long receiveUserId) {
        // 非空校验
        if (receiveUserId == null || receiveUserId <= 0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 调用DAO
        Integer count = messageMapper.selectUnreadCount(receiveUserId);
        if (count == null) {
            log.warn(ResultCode.ERROR_SERVICES.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
        // 返回结果
        return count;
    }

    @Override
    public List<Message> selectByReceiveUserId(Long receiveUserId) {
        // 非空校验
        if (receiveUserId == null || receiveUserId <= 0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 调用DAO
        List<Message> messages = messageMapper.selectByReceiveUserId(receiveUserId);
        // 返回结果
        return messages;
    }

    @Override
    public void updateStateById(Long id, Byte state) {
        // 非空校验
        if (id == null || id <= 0 || state < 0 || state > 2) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 构造更新对象
        Message updateMessage = new Message();
        updateMessage.setId(id);
        updateMessage.setState(state);
        Date date = new Date();
        updateMessage.setUpdateTime(date);
        // 调用DAO
        int row = messageMapper.updateByPrimaryKeySelective(updateMessage);
        if (row != 1) {
            log.warn(ResultCode.ERROR_SERVICES.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
    }

    @Override
    public Message selectById(Long id) {
        // 非空校验
        if (id == null || id <= 0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 调用DAO
        Message message = messageMapper.selectByPrimaryKey(id);
        // 返回结果
        return message;
    }

    @Override
    public void reply(Long replyId, Message message) {
        // 非空校验
        if (replyId == null || replyId <= 0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 校验repliedId对应的站内信状态
        Message existsMessage = messageMapper.selectByPrimaryKey(replyId);
        if (existsMessage == null || existsMessage.getDeleteState() == 1) {
            log.warn(ResultCode.FAILED_MESSAGE_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_MESSAGE_NOT_EXISTS));
        }
        // 更新状态为已回复
        updateStateById(replyId, (byte) 2);
        // 把回复的内容写进数据库
        create(message);
    }
}
