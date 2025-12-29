package com.campus.activity.service.impl;

import com.campus.activity.entity.Activity;
import com.campus.activity.repository.ActivityRepository;
import com.campus.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;  // 正确导入 java.util.Date
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired  // 添加这个注解
    private ActivityRepository activityRepository;  // 确保有这个字段

    @Override
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();  // 获取所有活动
    }

    @Override
    public List<Activity> getAvailableActivities() {
        List<Activity> availableActivities = new ArrayList<>();
        List<Activity> allActivities = activityRepository.findAll();

        Date now = new Date();  // 创建 java.util.Date 实例

        for (Activity activity : allActivities) {
            // 使用 Date 的 after() 方法进行比较
            if (activity.getEndTime() != null && activity.getEndTime().after(now)) {
                availableActivities.add(activity);
            }
        }

        return availableActivities;
    }
}