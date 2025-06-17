package cn.iocoder.forum.services;

import cn.iocoder.forum.model.User;
import org.apache.ibatis.annotations.Param;

public interface IUserService {
    /**
     * 创建普通用户
     * @param user 用户信息
     */
    void createNormalUser(User user);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUserName(String username);

    /**
     * 验证用户名和密码
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    User login(String username, String password);
}
