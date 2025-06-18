package com.bugtracker.bugtrack_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bugtracker.bugtrack_backend.entity.ActivityLog;
import com.bugtracker.bugtrack_backend.entity.User;
import com.bugtracker.bugtrack_backend.repository.ActivityLogRepository;
import com.bugtracker.bugtrack_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;

    public void logAction(Long userId, String action, Long referenceId) {
        User user = userRepository.findById(userId).orElseThrow();
        ActivityLog log = new ActivityLog();
        log.setUser(user);
        log.setAction(action);
        log.setReferenceId(referenceId);
        log.setTimestamp(LocalDateTime.now());
        activityLogRepository.save(log);
    }

    public List<ActivityLog> getAllLogs() {
        return activityLogRepository.findAll();
    }

    public List<ActivityLog> getLogsByUser(Long userId) {
        return activityLogRepository.findByUserId(userId);
    }
}
