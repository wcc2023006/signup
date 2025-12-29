// RegistrationService.java
package com.campus.activity.service;

import com.campus.activity.dto.RegistrationDTO;
import com.campus.activity.entity.Registration;
import java.util.List;

public interface RegistrationService {
    // 获取所有报名记录 - 返回DTO列表
    List<RegistrationDTO> getAllRegistrations();

    // 获取所有报名记录 - 返回实体列表（保留原有方法）
    List<Registration> getAllRegistrationEntities();

    // 根据ID获取报名记录 - DTO
    RegistrationDTO getRegistrationDTOById(Long id);

    // 根据ID获取报名记录 - 实体
    Registration getRegistrationById(Long id);

    // 用户报名活动
    Registration registerActivity(Long userId, Long activityId);

    // 取消报名
    Registration cancelRegistration(Long id);

    // 获取用户的所有报名记录 - DTO
    List<RegistrationDTO> getUserRegistrationDTOs(Long userId);

    // 获取用户的所有报名记录 - 实体
    List<Registration> getUserRegistrations(Long userId);

    // 获取活动的所有报名记录 - DTO
    List<RegistrationDTO> getActivityRegistrationDTOs(Long activityId);

    // 获取活动的所有报名记录 - 实体
    List<Registration> getActivityRegistrations(Long activityId);

    // 检查用户是否已报名某个活动
    boolean isUserRegistered(Long userId, Long activityId);

    // 获取活动的报名统计信息
    Object getActivityRegistrationStats(Long activityId);
}