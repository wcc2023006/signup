package com.campus.activity.service.impl;

import com.campus.activity.entity.User;
import com.campus.activity.repository.UserRepository;
import com.campus.activity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        // 设置创建时间和更新时间
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        // 设置默认角色（如果未提供）
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + id));

        // 更新字段
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }

        existingUser.setUpdatedAt(new Date());

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("用户不存在，ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean validateUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent() && userOptional.get().getPassword().equals(password);
    }
}