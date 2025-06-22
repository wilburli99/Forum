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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
