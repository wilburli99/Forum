package cn.iocoder.forum.controller;

import cn.iocoder.forum.common.AppResult;
import cn.iocoder.forum.common.ResultCode;
import cn.iocoder.forum.model.User;
import cn.iocoder.forum.services.IUserService;
import cn.iocoder.forum.utils.MD5Util;
import cn.iocoder.forum.utils.UUIDUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "用户接口", description = "用户注册、登录、登出、修改密码、修改昵称、修改头像、修改邮箱、修改手机号、修改简介、修改性别、修改生日、修改地址、修改密码、修改昵称、修改头像、修改邮箱、修改手机号、修改简介、修改性别、修改生日、修改地址")
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private IUserService userService;
    /**
     *  用户注册
     * @param username 用户名
     * @param nickname 昵称
     * @param password 密码
     * @param passwordRepeat 密码确认
     * @return
     */
    @Operation(summary = "用户注册", description = "用户注册")
    @PostMapping("register")        // 省去了多余的非空校验
    public AppResult register(@Param(value = "用户名") @RequestParam("username") @NotNull String username,
                              @Param(value = "昵称") @RequestParam("nickname") @NotNull String nickname,
                              @Param(value = "密码") @RequestParam("password") @NotNull String password,
                              @Param(value = "密码确认") @RequestParam("passwordRepeat") @NotNull String passwordRepeat) {

        // 校验密码是否与确认密码一致
        if (!password.equals(passwordRepeat)) {
            log.warn(ResultCode.FAILED_TWO_PWD_NOT_SAME.toString());
            return AppResult.failed(ResultCode.FAILED_TWO_PWD_NOT_SAME);
        }
        // 准备数据
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        // 密码加密
        // 1.生成盐
        String salt = UUIDUtil.uuid_32();
        // 2.生成密码的密文
        String md5Password = MD5Util.md5Salt(password, salt);
        // 设置密码和盐
        user.setPassword(md5Password);
        user.setSalt(salt);
        // 调用services层
        userService.createNormalUser(user);
        return AppResult.success();
    }
}
