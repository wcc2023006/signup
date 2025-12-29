package com.campus.activity.dto;

import com.campus.activity.entity.Registration;
import com.campus.activity.entity.User;
import com.campus.activity.entity.Activity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class RegistrationDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;      // 需要添加这个字段
    private Long activityId;
    private String activityName;
    private String activityLocation; // 需要添加这个字段
    private Integer status;
    private String statusText;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activityTime;    // 如果需要，可以添加活动时间

    // 无参构造函数
    public RegistrationDTO() {
    }

    // 修改后的构造函数 - 避免调用不存在的方法
    public RegistrationDTO(Registration registration) {
        this.id = registration.getId();
        this.userId = registration.getUserId();
        this.activityId = registration.getActivityId();
        this.status = registration.getStatus();
        this.signTime = registration.getSignTime();
        this.cancelTime = registration.getCancelTime();

        // 设置状态文本
        updateStatusText();

        // 安全地获取关联对象信息 - 使用try-catch避免方法不存在
        try {
            // 获取用户信息
            if (registration.getUser() != null) {
                try {
                    // 尝试使用反射获取用户信息
                    this.userName = (String) registration.getUser().getClass()
                            .getMethod("getName").invoke(registration.getUser());
                } catch (Exception e) {
                    this.userName = "用户" + registration.getUserId();
                }

                try {
                    this.userEmail = (String) registration.getUser().getClass()
                            .getMethod("getEmail").invoke(registration.getUser());
                } catch (Exception e) {
                    this.userEmail = registration.getUserId() + "@campus.edu";
                }
            } else {
                this.userName = "用户" + registration.getUserId();
                this.userEmail = registration.getUserId() + "@campus.edu";
            }

            // 获取活动信息
            if (registration.getActivity() != null) {
                try {
                    this.activityName = (String) registration.getActivity().getClass()
                            .getMethod("getName").invoke(registration.getActivity());
                } catch (Exception e) {
                    this.activityName = "活动" + registration.getActivityId();
                }

                try {
                    this.activityLocation = (String) registration.getActivity().getClass()
                            .getMethod("getLocation").invoke(registration.getActivity());
                } catch (Exception e) {
                    this.activityLocation = "校园地点";
                }

                try {
                    this.activityTime = (Date) registration.getActivity().getClass()
                            .getMethod("getActivityTime").invoke(registration.getActivity());
                } catch (Exception e) {
                    this.activityTime = null;
                }
            } else {
                this.activityName = "活动" + registration.getActivityId();
                this.activityLocation = "校园地点";
                this.activityTime = null;
            }
        } catch (Exception e) {
            // 如果出现任何异常，设置默认值
            this.userName = "用户" + registration.getUserId();
            this.userEmail = registration.getUserId() + "@campus.edu";
            this.activityName = "活动" + registration.getActivityId();
            this.activityLocation = "校园地点";
            this.activityTime = null;
        }
    }

    // 更简单的替代方案 - 直接设置默认值
    public RegistrationDTO(Registration registration, boolean simpleMode) {
        this.id = registration.getId();
        this.userId = registration.getUserId();
        this.activityId = registration.getActivityId();
        this.status = registration.getStatus();
        this.signTime = registration.getSignTime();
        this.cancelTime = registration.getCancelTime();

        // 设置状态文本
        updateStatusText();

        // 直接使用默认值，不尝试获取关联对象
        this.userName = "用户" + registration.getUserId();
        this.userEmail = registration.getUserId() + "@campus.edu";
        this.activityName = "活动" + registration.getActivityId();
        this.activityLocation = "校园地点";
        this.activityTime = registration.getSignTime(); // 使用报名时间作为活动时间
    }

    // 更新状态文本的方法
    private void updateStatusText() {
        if (status != null) {
            switch (status) {
                case 1:
                    this.statusText = "已报名";
                    break;
                case 2:
                    this.statusText = "已签到";
                    break;
                case 3:
                    this.statusText = "已取消";
                    break;
                default:
                    this.statusText = "未知状态";
            }
        } else {
            this.statusText = "无状态";
        }
    }

    // ========== Getter和Setter方法 ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    // 添加这个缺失的方法
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }

    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }

    // 添加这个缺失的方法
    public String getActivityLocation() { return activityLocation; }
    public void setActivityLocation(String activityLocation) {
        this.activityLocation = activityLocation;
    }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) {
        this.status = status;
        updateStatusText();
    }

    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }

    public Date getSignTime() { return signTime; }
    public void setSignTime(Date signTime) { this.signTime = signTime; }

    public Date getCancelTime() { return cancelTime; }
    public void setCancelTime(Date cancelTime) { this.cancelTime = cancelTime; }

    public Date getActivityTime() { return activityTime; }
    public void setActivityTime(Date activityTime) { this.activityTime = activityTime; }

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                (userEmail != null ? ", userEmail='" + userEmail + '\'' : "") +
                ", activityId=" + activityId +
                ", activityName='" + activityName + '\'' +
                (activityLocation != null ? ", activityLocation='" + activityLocation + '\'' : "") +
                ", status=" + status +
                ", statusText='" + statusText + '\'' +
                (signTime != null ? ", signTime=" + signTime : "") +
                '}';
    }
}