package com.campus.activity.repository;

import com.campus.activity.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    // Java 8 兼容：返回 Registration（不是 Optional）
    Registration findByUserIdAndActivityId(Long userId, Long activityId);

    List<Registration> findByUserId(Long userId);
    List<Registration> findByActivityId(Long activityId);
    Long countByActivityId(Long activityId);
    Long countByActivityIdAndStatus(Long activityId, Integer status);
}