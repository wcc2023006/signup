// 文件：ActivityController.java
// 确保包路径正确

package com.campus.activity.controller;

import com.campus.activity.service.ActivityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/activities")
    public Map<String, Object> getAllActivities() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", activityService.getAllActivities());
        return result;
    }

    @GetMapping("/activities/available")
    public Map<String, Object> getAvailableActivities() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", activityService.getAvailableActivities());
        return result;
    }
}