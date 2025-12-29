package com.campus.activity.service.impl;

import com.campus.activity.dto.RegistrationDTO;
import com.campus.activity.entity.Registration;
import com.campus.activity.entity.User;
import com.campus.activity.entity.Activity;
import com.campus.activity.repository.RegistrationRepository;
import com.campus.activity.repository.UserRepository;
import com.campus.activity.repository.ActivityRepository;
import com.campus.activity.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    // 状态常量
    private static final int STATUS_REGISTERED = 1;
    private static final int STATUS_SIGNED = 2;
    private static final int STATUS_CANCELLED = 3;

    // === 所有方法都使用 Java 8 兼容语法 ===

    @Override
    public List<RegistrationDTO> getAllRegistrations() {
        try {
            List<Registration> registrations = registrationRepository.findAll();
            List<RegistrationDTO> dtos = new ArrayList<>();
            for (Registration reg : registrations) {
                dtos.add(new RegistrationDTO(reg));
            }
            return dtos;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Registration> getAllRegistrationEntities() {
        try {
            return registrationRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public RegistrationDTO getRegistrationDTOById(Long id) {
        try {
            Optional<Registration> optional = registrationRepository.findById(id);
            if (optional.isPresent()) {
                return new RegistrationDTO(optional.get());
            } else {
                throw new RuntimeException("报名记录不存在");
            }
        } catch (Exception e) {
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    @Override
    public Registration getRegistrationById(Long id) {
        try {
            Optional<Registration> optional = registrationRepository.findById(id);
            if (optional.isPresent()) {
                return optional.get();
            } else {
                throw new RuntimeException("报名记录不存在");
            }
        } catch (Exception e) {
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Registration registerActivity(Long userId, Long activityId) {
        try {
            // 1. 验证用户 - Java 8 兼容写法
            Optional<User> userOpt = userRepository.findById(userId);
            if (!userOpt.isPresent()) {
                throw new RuntimeException("用户不存在");
            }
            User user = userOpt.get();

            // 2. 验证活动 - Java 8 兼容写法
            Optional<Activity> activityOpt = activityRepository.findById(activityId);
            if (!activityOpt.isPresent()) {
                throw new RuntimeException("活动不存在");
            }
            Activity activity = activityOpt.get();

            // 3. 检查是否已报名 - 假设Repository返回Registration（不是Optional）
            Registration existing = registrationRepository.findByUserIdAndActivityId(userId, activityId);
            if (existing != null && existing.getStatus() != STATUS_CANCELLED) {
                throw new RuntimeException("您已经报名过此活动");
            }

            // 4. 检查名额
            long currentCount = registrationRepository.countByActivityIdAndStatus(activityId, STATUS_REGISTERED);
            if (currentCount >= activity.getMaxParticipants()) {
                throw new RuntimeException("活动名额已满");
            }

            // 5. 创建或更新报名记录
            Registration registration;
            if (existing != null && existing.getStatus() == STATUS_CANCELLED) {
                // 更新已取消的记录
                registration = existing;
                registration.setStatus(STATUS_REGISTERED);
                registration.setSignTime(new Date());
                registration.setCancelTime(null);
            } else {
                // 创建新记录
                registration = new Registration();
                registration.setUser(user);
                registration.setActivity(activity);
                registration.setUserId(userId);
                registration.setActivityId(activityId);
                registration.setStatus(STATUS_REGISTERED);
                registration.setSignTime(new Date());
            }

            Registration saved = registrationRepository.save(registration);

            // 6. 更新活动人数
            activity.setCurrentParticipants((int) (currentCount + 1));
            activityRepository.save(activity);

            return saved;

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("报名失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Registration cancelRegistration(Long id) {
        try {
            Optional<Registration> registrationOpt = registrationRepository.findById(id);
            if (!registrationOpt.isPresent()) {
                throw new RuntimeException("报名记录不存在");
            }

            Registration registration = registrationOpt.get();

            if (registration.getStatus() == STATUS_CANCELLED) {
                throw new RuntimeException("报名已取消，无需重复操作");
            }

            if (registration.getStatus() != STATUS_REGISTERED) {
                throw new RuntimeException("只有已报名的活动可以取消");
            }

            registration.setStatus(STATUS_CANCELLED);
            registration.setCancelTime(new Date());

            if (registration.getActivity() != null) {
                Activity activity = registration.getActivity();
                long currentCount = registrationRepository.countByActivityIdAndStatus(
                        activity.getId(), STATUS_REGISTERED);
                activity.setCurrentParticipants((int) Math.max(0, currentCount - 1));
                activityRepository.save(activity);
            }

            return registrationRepository.save(registration);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("取消失败: " + e.getMessage());
        }
    }

    @Override
    public List<RegistrationDTO> getUserRegistrationDTOs(Long userId) {
        try {
            List<Registration> registrations = registrationRepository.findByUserId(userId);
            List<RegistrationDTO> dtos = new ArrayList<>();
            for (Registration reg : registrations) {
                dtos.add(new RegistrationDTO(reg));
            }
            return dtos;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Registration> getUserRegistrations(Long userId) {
        try {
            return registrationRepository.findByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<RegistrationDTO> getActivityRegistrationDTOs(Long activityId) {
        try {
            List<Registration> registrations = registrationRepository.findByActivityId(activityId);
            List<RegistrationDTO> dtos = new ArrayList<>();
            for (Registration reg : registrations) {
                dtos.add(new RegistrationDTO(reg));
            }
            return dtos;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Registration> getActivityRegistrations(Long activityId) {
        try {
            return registrationRepository.findByActivityId(activityId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isUserRegistered(Long userId, Long activityId) {
        try {
            // 假设Repository返回Registration（不是Optional）
            Registration registration = registrationRepository.findByUserIdAndActivityId(userId, activityId);
            return registration != null && registration.getStatus() == STATUS_REGISTERED;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object getActivityRegistrationStats(Long activityId) {
        Map<String, Object> stats = new HashMap<>();

        try {
            long totalCount = registrationRepository.countByActivityId(activityId);
            long registeredCount = registrationRepository.countByActivityIdAndStatus(activityId, STATUS_REGISTERED);
            long signedCount = registrationRepository.countByActivityIdAndStatus(activityId, STATUS_SIGNED);
            long cancelledCount = registrationRepository.countByActivityIdAndStatus(activityId, STATUS_CANCELLED);

            stats.put("total", totalCount);
            stats.put("registered", registeredCount);
            stats.put("signed", signedCount);
            stats.put("cancelled", cancelledCount);
            stats.put("registeredPercentage", totalCount > 0 ? (double) registeredCount / totalCount * 100 : 0);
            stats.put("signedPercentage", totalCount > 0 ? (double) signedCount / totalCount * 100 : 0);
            stats.put("cancelledPercentage", totalCount > 0 ? (double) cancelledCount / totalCount * 100 : 0);

        } catch (Exception e) {
            e.printStackTrace();
            stats.put("total", 0);
            stats.put("registered", 0);
            stats.put("signed", 0);
            stats.put("cancelled", 0);
            stats.put("registeredPercentage", 0.0);
            stats.put("signedPercentage", 0.0);
            stats.put("cancelledPercentage", 0.0);
        }

        return stats;
    }

    @Transactional
    public Registration signIn(Long registrationId) {
        try {
            Optional<Registration> registrationOpt = registrationRepository.findById(registrationId);
            if (!registrationOpt.isPresent()) {
                throw new RuntimeException("报名记录不存在");
            }

            Registration registration = registrationOpt.get();
            if (registration.getStatus() == STATUS_CANCELLED) {
                throw new RuntimeException("已取消的报名不能签到");
            }

            if (registration.getStatus() == STATUS_SIGNED) {
                throw new RuntimeException("已经签到过了");
            }

            registration.setStatus(STATUS_SIGNED);
            return registrationRepository.save(registration);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("签到失败: " + e.getMessage());
        }
    }

    public Integer getUserRegistrationStatus(Long userId, Long activityId) {
        try {
            // 假设Repository返回Registration（不是Optional）
            Registration registration = registrationRepository.findByUserIdAndActivityId(userId, activityId);
            return registration != null ? registration.getStatus() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}