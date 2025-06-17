package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.common.AppResult;
import cn.iocoder.forum.common.ResultCode;
import cn.iocoder.forum.dao.UserMapper;
import cn.iocoder.forum.exception.ApplicationException;
import cn.iocoder.forum.model.User;
import cn.iocoder.forum.services.IUserService;
import cn.iocoder.forum.utils.MD5Util;
import cn.iocoder.forum.utils.StringUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public void createNormalUser(User user) {
        //1. 非空校验
        if (user == null || StringUtil.isEmpty(user.getUsername())
                || StringUtil.isEmpty(user.getPassword())
                || StringUtil.isEmpty(user.getNickname())
                || StringUtil.isEmpty(user.getSalt())) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        //2. 按用户名查询用户信息
        User existUser = userMapper.selectByUserName(user.getUsername());
        // 判断用户信息是否存在
        if (existUser != null) {
            log.info(ResultCode.FAILED_USER_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_EXISTS));
        }
        //3. 新增用户流程，设置默认值
        user.getGender(2);
        user.getIsAdmin(0);
        user.getState(0);
        user.getArticleCount(0);
        user.getDeleteState(0);
        // 当前日期
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        // 写入数据库
        int row = userMapper.insertSelective(user);
        if (row != 1) {
            log.info(ResultCode.FAILED_CREATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        // 打印日志
        log.info("新用户成功. username = " + user.getUsername() + ". ");
    }

    @Override
    public User selectByUserName(String username) {
        if (StringUtil.isEmpty(username)) {
            log.info(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        return userMapper.selectByUserName(username);
    }

    @Override
    public User login(String username, String password) {
        // 1.非空校验
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            log.info(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 2.按照用户名查询信息
        User user = userMapper.selectByUserName(username);
        // 3.对查询结果做非空校验
        if (user == null) {
            // 如果用户不存在
            log.info(ResultCode.FAILED_LOGIN.toString() + "username = " + username);
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }
        // 4.密码校验
        String md5Password = MD5Util.md5Salt(password, user.getSalt());
        // 如果密码不一致，则返回登录失败
        if (!md5Password.equalsIgnoreCase(user.getPassword())) {
            log.info(ResultCode.FAILED_LOGIN.toString() + "密码错误, username = " +  username);
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }
        // 登录成功，返回用户信息
        log.info("登录成功, username = " +  username);
        return user;
    }

    @Override
    public User selectById(Long id) {
        // 1. 非空校验
        if (id == null) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 2. 查询用户
        User user = userMapper.selectByPrimaryKey(id);
        // 3. 返回结果
        return user;
    }

    @Override
    public void addOneArticleCountById(Long id) {
        if (id == null || id <= 0) {
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }
        // 查询用户
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            log.warn(ResultCode.ERROR_IS_NULL.toString() + "userId = " + id);
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 更新用户的发帖数量
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setArticleCount(user.getArticleCount() + 1);
        // 调用DAO层，执行更新
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        // 判断受影响的行数
        if (row != 1) {
            log.warn(ResultCode.FAILED.toString() + "受影响的行数不等于1" + row);
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }
}
