package cn.iocoder.forum.controller;

import cn.iocoder.forum.common.AppResult;
import cn.iocoder.forum.common.ResultCode;
import cn.iocoder.forum.config.AppConfig;
import cn.iocoder.forum.model.Message;
import cn.iocoder.forum.model.User;
import cn.iocoder.forum.services.IMessageService;
import cn.iocoder.forum.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "站内信接口", description = "站内信接口")
@Slf4j
@RestController
@RequestMapping("message")
public class MessageController {
    @Resource
    private IMessageService messageService;
    @Resource
    private IUserService userService;

    @Operation(summary = "发送站内信", description = "发送站内信")
    @PostMapping("send")
    public AppResult send(HttpServletRequest request,
                          @Param("接收者ID") @RequestParam("receiveUserId") @NotNull Long receiveUserId,
                          @Param("内容") @RequestParam("content") @NotNull String content) {
        // 获取用户信息
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 1. 当前登录用户的状态，如果是禁言状态不能发站内信
        if (user.getState() == 1) {
            // 返回用户状态异常
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        // 2. 不能给自己发站内信
        if (user.getId() == receiveUserId) {
            return AppResult.failed("不能给自己发送站内信");
        }
        // 3. 校验接收者是否存在
        User receiveUser = userService.selectById(receiveUserId);
        if (receiveUser == null || receiveUser.getDeleteState() == 1) {
            // 返回接收者状态不正常
            return AppResult.failed("接收者状态异常");
        }
        // 4. 封装对象
        Message message = new Message();
        message.setPostUserId(user.getId()); // 发送者ID
        message.setReceiveUserId(receiveUserId); // 接受者ID
        message.setContent(content); //  内容
        // 5. 调用service
        messageService.create(message);
        // 6. 返回结果
        return AppResult.success("发送成功");
    }

    @Operation(summary = "获取未读消息数量", description = "获取未读消息数量")
    @GetMapping("/getUnreadCount")
    public AppResult<Integer> getUnreadCount(HttpServletRequest request) {
        // 1. 获取当前登录的用户
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 2. 调用service
        Integer count = messageService.selectUnreadCount(user.getId());
        // 3. 返回结果
        return AppResult.success(count);
    }
    @Operation(summary = "获取用户所有站内信消息", description = "获取用户所有站内信消息")
    @GetMapping("/getAll")
    public AppResult<List<Message>> getAll(HttpServletRequest request) {
        // 1. 获取当前登录的用户
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 2. 调用service
        List<Message> messages = messageService.selectByReceiveUserId(user.getId());
        // 3. 封装结果
        return AppResult.success(messages);
    }

    @Operation(summary = "标记已读", description = "标记已读")
    @PostMapping("/markRead")
    public AppResult markRead(HttpServletRequest request,
                               @Param("站内信id") @RequestParam("id") @NotNull Long id) {
        // 1. 根据ID查询站内信
        Message message = messageService.selectById(id);
        // 2. 站内信是否存在
        if (message == null || message.getDeleteState() == 1) {
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_MESSAGE_NOT_EXISTS);
        }
        // 3. 站内信是不是自己的
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        if (user.getId() != message.getReceiveUserId()) {
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_FORBIDDEN);
        }
        // 4. 调用service中的方法，更新为已读
        messageService.updateStateById(id, (byte) 1);
        // 5. 返回结果
        return AppResult.success();
    }
}
