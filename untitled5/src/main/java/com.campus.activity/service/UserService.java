package com.campus.activity.service;

import com.campus.activity.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // 获取所有用户
    List<User> getAllUsers();

    // 根据ID获取用户
    Optional<User> getUserById(Long id);

    // 根据用户名获取用户
    Optional<User> getUserByUsername(String username);

    // 创建新用户
    User createUser(User user);

    // 更新用户信息
    User updateUser(Long id, User user);

    // 删除用户
    void deleteUser(Long id);

    // 验证用户登录
    boolean validateUser(String username, String password);
}