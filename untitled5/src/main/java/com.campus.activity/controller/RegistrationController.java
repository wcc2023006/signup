// RegistrationController.java - 完整修改版
package com.campus.activity.controller;

import com.campus.activity.dto.RegistrationDTO;
import com.campus.activity.entity.Registration;
import com.campus.activity.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    // 1. 获取所有报名记录（返回DTO格式）
    @GetMapping
    public Map<String, Object> getAllRegistrations() {
        Map<String, Object> result = new HashMap<>();

        try {
            List<RegistrationDTO> registrations = registrationService.getAllRegistrations();

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", registrations);
            result.put("count", registrations.size());

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "服务器错误: " + e.getMessage());
            result.put("data", new java.util.ArrayList<>());
            result.put("count", 0);
        }

        return result;
    }

    // 2. 根据ID获取报名记录（DTO格式）
    @GetMapping("/{id}")
    public Map<String, Object> getRegistrationById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();

        try {
            RegistrationDTO registration = registrationService.getRegistrationDTOById(id);

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", registration);
        } catch (RuntimeException e) {
            result.put("code", 404);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    // 3. 用户报名活动（保持原样）
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> registerActivity(@RequestBody Map<String, Long> request) {
        Map<String, Object> result = new HashMap<>();

        Long userId = request.get("userId");
        Long activityId = request.get("activityId");

        if (userId == null || activityId == null) {
            result.put("code", 400);
            result.put("message", "参数错误：userId和activityId不能为空");
            result.put("data", null);
            return result;
        }

        try {
            Registration registration = registrationService.registerActivity(userId, activityId);

            // 转换为DTO返回
            RegistrationDTO registrationDTO = new RegistrationDTO(registration);

            result.put("code", 201);
            result.put("message", "报名成功");
            result.put("data", registrationDTO);
        } catch (RuntimeException e) {
            result.put("code", 400);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    // 4. 取消报名
    @PostMapping("/{id}/cancel")
    public Map<String, Object> cancelRegistration(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();

        try {
            Registration registration = registrationService.cancelRegistration(id);

            // 转换为DTO返回
            RegistrationDTO registrationDTO = new RegistrationDTO(registration);

            result.put("code", 200);
            result.put("message", "取消报名成功");
            result.put("data", registrationDTO);
        } catch (RuntimeException e) {
            result.put("code", 400);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    // 5. 获取用户的所有报名记录（DTO格式）
    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserRegistrations(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<RegistrationDTO> registrations = registrationService.getUserRegistrationDTOs(userId);

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", registrations);
            result.put("count", registrations.size());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
            result.put("data", new java.util.ArrayList<>());
            result.put("count", 0);
        }

        return result;
    }

    // 6. 获取活动的所有报名记录（DTO格式）
    @GetMapping("/activity/{activityId}")
    public Map<String, Object> getActivityRegistrations(@PathVariable Long activityId) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<RegistrationDTO> registrations = registrationService.getActivityRegistrationDTOs(activityId);

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", registrations);
            result.put("count", registrations.size());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
            result.put("data", new java.util.ArrayList<>());
            result.put("count", 0);
        }

        return result;
    }

    // 7. 检查用户是否已报名某个活动
    @GetMapping("/check")
    public Map<String, Object> checkRegistration(@RequestParam Long userId,
                                                 @RequestParam Long activityId) {
        Map<String, Object> result = new HashMap<>();

        boolean isRegistered = registrationService.isUserRegistered(userId, activityId);

        // 创建数据Map
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("activityId", activityId);
        data.put("isRegistered", isRegistered);

        result.put("code", 200);
        result.put("message", "success");
        result.put("data", data);

        return result;
    }

    // 8. 获取活动的报名统计信息
    @GetMapping("/activity/{activityId}/stats")
    public Map<String, Object> getActivityStats(@PathVariable Long activityId) {
        Map<String, Object> result = new HashMap<>();

        try {
            Object stats = registrationService.getActivityRegistrationStats(activityId);

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", stats);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取统计信息失败: " + e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    // 9. 获取所有报名记录（实体格式 - 兼容旧版本）
    @GetMapping("/entities")
    public Map<String, Object> getAllRegistrationEntities() {
        Map<String, Object> result = new HashMap<>();

        try {
            List<Registration> registrations = registrationService.getAllRegistrationEntities();

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", registrations);
            result.put("count", registrations.size());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
            result.put("data", new java.util.ArrayList<>());
            result.put("count", 0);
        }

        return result;
    }

    // 10. 测试接口
    @GetMapping("/test/dto")
    public Map<String, Object> testDTO() {
        Map<String, Object> result = new HashMap<>();

        // 创建一个测试DTO
        // 1. 首先创建DTO对象（确保变量名正确）
        RegistrationDTO testDTO = new RegistrationDTO();

// 2. 设置属性（使用字母O，不是数字0）
        testDTO.setUserId(1L);
        testDTO.setUserName("测试用户");
        testDTO.setUserEmail("test@campus.edu");
        testDTO.setActivityId(1L);
        testDTO.setActivityName("校园歌手大赛");
        testDTO.setActivityLocation("大学生活动中心");
        testDTO.setStatus(1);
        testDTO.setSignTime(new java.util.Date());

// 3. 创建返回结果

        result.put("code", 200);
        result.put("message", "DTO测试成功");
        result.put("data", testDTO);

        return result;
    }
}