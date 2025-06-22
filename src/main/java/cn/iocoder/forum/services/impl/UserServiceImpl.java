package cn.iocoder.forum.services.impl;

import cn.iocoder.forum.common.AppResult;
import cn.iocoder.forum.common.ResultCode;
import cn.iocoder.forum.dao.UserMapper;
import cn.iocoder.forum.exception.ApplicationException;
import cn.iocoder.forum.model.User;
import cn.iocoder.forum.services.IUserService;
import cn.iocoder.forum.utils.MD5Util;
import cn.iocoder.forum.utils.StringUtil;
import cn.iocoder.forum.utils.UUIDUtil;
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
            log.warn(ResultCode.ERROR_IS_NULL.toString() + "user Id = " + id);
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

    @Override
    public void subOneArticleCountById(Long id) {
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
        updateUser.setArticleCount(user.getArticleCount() - 1);
        // 判断-1之后是否小于0
        if (updateUser.getArticleCount() < 0) {
            // 如果小于0，则设置为0
            updateUser.setArticleCount(0);
        }
        // 调用DAO层，执行更新
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        // 判断受影响的行数
        if (row != 1) {
            log.warn(ResultCode.FAILED.toString() + "受影响的行数不等于1" + row);
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }

    @Override
    public void modifyInfo(User user) {
        // 1. 非空校验
        if (user == null || user.getId() == null || user.getId() <= 0) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 2. 校验用户是否存在
        User existsUser = userMapper.selectByPrimaryKey(user.getId());
        if (existsUser == null) {
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }
        // 3. 定义一个标志位
        boolean checkAttr = false; // false表示没有校验通过

        // 4. 定义一个专门用来更新的对象，防止用户传入的User对象设置了其他的属性
        // 当使用动态SQL进行更新的时候，覆盖了没有经过校验的字段
        User updateUser = new User();
        // 5. 设置用户ID
        updateUser.setId(user.getId());

        // 6. 对每一个参数进行校验并赋值
        if (!StringUtil.isEmpty(user.getUsername())
                && !user.getUsername().equals(existsUser.getUsername())) {
            // 需要更新用户名(登录名)时，进行唯一性的校验
            User checkUser = userMapper.selectByUserName(user.getUsername());
            if (checkUser != null) {
                // 用户已存在
                log.warn(ResultCode.FAILED_USER_EXISTS.toString());
                throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_EXISTS));
            }
            // 数据库中没有找到相应的用户，表示可以修改用户名
            updateUser.setUsername(user.getUsername());
            // 更新标志位
            checkAttr = true;
        }

        // 7. 校验昵称
        if (!StringUtil.isEmpty(user.getNickname())
            && !user.getNickname().equals(existsUser.getNickname())) {
            // 设置昵称
            updateUser.setNickname(user.getNickname());
            // 更新标志位
            checkAttr = true;
        }

        // 8. 校验性别
        if (user.getGender() != null && user.getGender() != existsUser.getGender()) {
            // 设置性别
            updateUser.setGender(user.getGender());
            // 合法性校验
            if (updateUser.getGender() > 2 || updateUser.getGender() < 0) {
                updateUser.setGender((byte) 2);
            }
            // 更新标志位
            checkAttr = true;
        }

        // 9. 校验邮箱
        if (!StringUtil.isEmpty(user.getEmail())
                && !user.getEmail().equals(existsUser.getEmail())) {
            // 设置邮箱
            updateUser.setEmail(user.getEmail());
            // 更新标志位
            checkAttr = true;
        }

        // 10. 校验手机号码
        if (!StringUtil.isEmpty(user.getPhoneNum())
                && !user.getPhoneNum().equals(existsUser.getPhoneNum())) {
            // 设置电话号码
            updateUser.setPhoneNum(user.getPhoneNum());
            // 更新标志位
            checkAttr = true;
        }

        // 11. 校验个人简介
        if (!StringUtil.isEmpty(user.getRemark())
                && !user.getRemark().equals(existsUser.getRemark())) {
            // 设置电话号码
            updateUser.setRemark(user.getRemark());
            // 更新标志位
            checkAttr = true;
        }

        // 12. 根据标志位来决定是否更新
        if (checkAttr == false) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }

        // 调用DAO
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            log.warn(ResultCode.FAILED.toString() + ", 受影响的行数不等于 1 .");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }

    @Override
    public void modifyPassword(Long id, String oldPassword, String newPassword) {
        // 非空校验
        if (id == null || id <= 0 || StringUtil.isEmpty(oldPassword) || StringUtil.isEmpty(newPassword)) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 查询用户信息
        User user = userMapper.selectByPrimaryKey(id);
        // 校验用户是否存在
        if (user == null || user.getDeleteState() == 1) {
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }
        // 校验老密码是否正确
        // 对老密码进行加密，获取密文
        String oldMd5Password = MD5Util.md5Salt(oldPassword, user.getSalt());
        // 与用户当前的密码进行比较
        if (!oldMd5Password.equalsIgnoreCase(user.getPassword())) {
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 生成一个新的盐值
        String salt = UUIDUtil.uuid_32();
        // 生成新密码的密文
        String md5Password = MD5Util.md5Salt(newPassword, salt);
        // 构造要更新的对象
        User updateUser = new User();
        updateUser.setId(user.getId()); // 用户Id
        updateUser.setSalt(salt); // 新生成的盐
        updateUser.setPassword(md5Password); // 新密码对应的密文
        Date date = new Date();
        updateUser.setUpdateTime(date); // 更新时间
        // 调用DAO层，执行更新
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            log.warn(ResultCode.FAILED.toString() + ", 受影响的行数不等于 1 .");
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }
}
