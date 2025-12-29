// 位置：src/main/java/com/campus/activity/service/ActivityService.java

package com.campus.activity.service;

import com.campus.activity.entity.Activity;

import java.util.List;

// 应该修改为具体的实体类
public interface ActivityService {
    List<Activity> getAllActivities();  // 改为 Activity 类型
    List<Activity> getAvailableActivities();
}