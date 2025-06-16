package cn.iocoder.forum.services;

import cn.iocoder.forum.model.User;

public interface IUserService {
    /**
     * 创建普通用户
     * @param user 用户信息
     */
    void createNormalUser(User user);
}
