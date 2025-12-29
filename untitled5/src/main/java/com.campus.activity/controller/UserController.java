package com.campus.activity.controller;

import com.campus.activity.entity.User;
import com.campus.activity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 获取所有用户
    @GetMapping
    public Map<String, Object> getAllUsers() {
        Map<String, Object> result = new HashMap<>();
        List<User> users = userService.getAllUsers();

        result.put("code", 200);
        result.put("message", "success");
        result.put("data", users);
        result.put("count", users.size());

        return result;
    }

    // 根据ID获取用户
    @GetMapping("/{id}")
    public Map<String, Object> getUserById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();

        return userService.getUserById(id)
                .map(user -> {
                    result.put("code", 200);
                    result.put("message", "success");
                    result.put("data", user);
                    return result;
                })
                .orElseGet(() -> {
                    result.put("code", 404);
                    result.put("message", "用户不存在");
                    result.put("data", null);
                    return result;
                });
    }

    // 创建用户
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();

        User createdUser = userService.createUser(user);

        result.put("code", 201);
        result.put("message", "用户创建成功");
        result.put("data", createdUser);

        return result;
    }

    // 更新用户
    @PutMapping("/{id}")
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody User user) {
        Map<String, Object> result = new HashMap<>();

        try {
            User updatedUser = userService.updateUser(id, user);

            result.put("code", 200);
            result.put("message", "用户更新成功");
            result.put("data", updatedUser);
        } catch (RuntimeException e) {
            result.put("code", 404);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();

        try {
            userService.deleteUser(id);

            result.put("code", 200);
            result.put("message", "用户删除成功");
            result.put("data", null);
        } catch (RuntimeException e) {
            result.put("code", 404);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    // 用户登录
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginRequest) {
        Map<String, Object> result = new HashMap<>();

        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        boolean isValid = userService.validateUser(username, password);

        if (isValid) {
            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("data", userService.getUserByUsername(username).orElse(null));
        } else {
            result.put("code", 401);
            result.put("message", "用户名或密码错误");
            result.put("data", null);
        }

        return result;
    }
}
