package com.bugtracker.bugtrack_backend.repository;

import com.bugtracker.bugtrack_backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientIdOrderByTimestampDesc(Long recipientId);
}
