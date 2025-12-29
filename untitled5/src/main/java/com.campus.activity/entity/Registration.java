// Registration.java - 确认实体类正确
package com.campus.activity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "registrations")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore  // 防止序列化问题
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    @JsonIgnore  // 防止序列化问题
    private Activity activity;

    @Column(name = "activity_id", insertable = false, updatable = false)
    private Long activityId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "sign_time")
    private Date signTime;

    @Column(name = "cancel_time")
    private Date cancelTime;

    // 必须有无参构造器
    public Registration() {}

    // 有参构造器（可选）
    public Registration(Long id, Long userId, Long activityId,
                        Integer status, Date signTime, Date cancelTime) {
        this.id = id;
        this.userId = userId;
        this.activityId = activityId;
        this.status = status;
        this.signTime = signTime;
        this.cancelTime = cancelTime;
    }

    // getter和setter...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Activity getActivity() { return activity; }
    public void setActivity(Activity activity) { this.activity = activity; }

    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Date getSignTime() { return signTime; }
    public void setSignTime(Date signTime) { this.signTime = signTime; }

    public Date getCancelTime() { return cancelTime; }
    public void setCancelTime(Date cancelTime) { this.cancelTime = cancelTime; }
}