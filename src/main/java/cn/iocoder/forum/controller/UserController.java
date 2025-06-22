package cn.iocoder.forum.controller;

import cn.iocoder.forum.common.AppResult;
import cn.iocoder.forum.common.ResultCode;
import cn.iocoder.forum.config.AppConfig;
import cn.iocoder.forum.model.User;
import cn.iocoder.forum.services.IUserService;
import cn.iocoder.forum.utils.MD5Util;
import cn.iocoder.forum.utils.StringUtil;
import cn.iocoder.forum.utils.UUIDUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "用户登录", description = "用户登录")
    @PostMapping("/login")
    public AppResult login(HttpServletRequest request,
                           @Param("用户名") @RequestParam("username") @NotNull String username,
                           @Param("密码") @RequestParam("password") @NotNull String password) {
        // 1. 调用services中的登录方法，返回user对象
        User user = userService.login(username, password);
        // 二次校验
        if (user == null) {
            log.warn(ResultCode.FAILED_LOGIN.toString());
            return AppResult.failed(ResultCode.FAILED_LOGIN);
        }
        // 2. 如果登录成功把User对象设置到Session作用域中
        HttpSession session = request.getSession();
        session.setAttribute(AppConfig.USER_SESSION, user);
        // 3. 创建返回结果
        return AppResult.success(user);
    }

    @Operation(summary ="获取用户信息", description = "获取用户信息")
    @GetMapping("/info")
    public AppResult<User> getUserInfo (HttpServletRequest request,
                                        @Param("用户Id") @RequestParam(value = "id", required = false) Long id) {
        // 定义返回的User对象
        User user = null;
        // 根据Id的值判断User对象的获取方式
        if (id == null) {
            // 1. 如果id为空，从session中获取当前登录的用户信息
            HttpSession session = request.getSession(false);
            // 从session中获取当前登录的用户信息
            user = (User) session.getAttribute(AppConfig.USER_SESSION);
        } else {
            // 2. 如果id不为空，从数据库中按Id查询出用户信息
            user = userService.selectById(id);
        }
        // 判断用户对象是否为空
        if (user == null) {
            return AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS);
        }
        // 返回正常的结果
        return AppResult.success(user);
    }

    @Operation(summary = "用户登出", description = "用户登出")
    @GetMapping("/logout")
    public AppResult logout(HttpServletRequest  request) {
        // 获取session对象
        HttpSession session = request.getSession(false);
        // 判断session对象是否为空
        if (session != null) {
            log.info("用户登出");
            // 表示用户在登录状态，直接销毁session
            session.invalidate();
        }
        return AppResult.success("退出登录成功");
    }

    @Operation(summary = "修改用户信息", description = "修改用户信息")
    @PostMapping("/modifyInfo")
    public AppResult modifyInfo(HttpServletRequest request,
                                @Param("用户名") @RequestParam(value = "username",required = false) String username,
                                @Param("昵称") @RequestParam(value = "nickname",required = false) String nickname,
                                @Param("性别") @RequestParam(value = "gender",required = false) Byte gender,
                                @Param("邮箱") @RequestParam(value = "email",required = false) String email,
                                @Param("电话号") @RequestParam(value = "phoneNum",required = false) String phoneNum,
                                @Param("个人简介") @RequestParam(value = "remark",required = false) String remark) {
        // 1. 接收参数
        // 2. 对参数做非空校验（全部都为空，则返回错误描述）
        if (StringUtil.isEmpty(username) && StringUtil.isEmpty(nickname)
                && StringUtil.isEmpty(email) && StringUtil.isEmpty(phoneNum)
                && StringUtil.isEmpty(remark) && gender == null) {
            // 返回错误信息
            return AppResult.failed("请输入要修改的内容");
        }
        // 从session中获取用户信息
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 3. 封装对象
        User updateUser = new User();
        updateUser.setId(user.getId()); // 用户Id
        updateUser.setUsername(username); // 用户名
        updateUser.setNickname(nickname); // 昵称
        updateUser.setGender(gender); // 性别
        updateUser.setEmail(email); // 邮箱
        updateUser.setPhoneNum(phoneNum); // 电话
        updateUser.setRemark(remark); // 个人简介
        // 4. 调用service中的方法
        userService.modifyInfo(updateUser);
        // 5. 查询最新的用户
        user = userService.selectById(user.getId());
        // 6. 把最新的用户信息设置到session中
        session.setAttribute(AppConfig.USER_SESSION, user);
        // 7. 返回结果
        return AppResult.success(user);
    }

    @Operation(summary = "修改用户密码", description = "修改用户密码")
    @PostMapping("/modifyPwd")
    public AppResult modifyPassword(HttpServletRequest request,
                                    @Param("旧密码") @RequestParam("oldPassword") @NotNull String oldPassword,
                                    @Param("新密码") @RequestParam("newPassword") @NotNull String newPassword,
                                    @Param("确认密码") @RequestParam("passwordRepeat") @NotNull String passwordRepeat) {
        // 1. 校验新密码与老密码是否相同
        if (newPassword.equals(oldPassword)) {
            return AppResult.failed(ResultCode.FAILED_TWO_PWD_NOT_SAME);
        }
        // 2. 获取session中的用户信息
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 3. 调用service中的方法
        userService.modifyPassword(user.getId(), oldPassword, newPassword);
        // 4. 销毁session, 强制让用户重新登录
        if (session != null) {
            session.invalidate();
        }
        // 5. 返回结果
        return AppResult.success();
    }
}
