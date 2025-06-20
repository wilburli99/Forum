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

    /**
     * 根据 id 查询用户
     * @param id  id
     * @return user对象
     */
    User selectById(Long id);

    /**
     * 用户发帖数 +1
     * @param id 用户Id
     */
    void addOneArticleCountById(Long id);

    /**
     * 用户发帖数 -1
     * @param id 用户Id
     */
    void subOneArticleCountById(Long id);
}
