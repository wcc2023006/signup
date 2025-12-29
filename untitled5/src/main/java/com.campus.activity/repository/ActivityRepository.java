package com.campus.activity.repository;

import com.campus.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByCollege(String college);
    List<Activity> findByStatus(Integer status);
}