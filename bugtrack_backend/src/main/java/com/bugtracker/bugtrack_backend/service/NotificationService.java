package com.bugtracker.bugtrack_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.bugtracker.bugtrack_backend.dto.NotificationDTO;
import com.bugtracker.bugtrack_backend.entity.Notification;
import com.bugtracker.bugtrack_backend.entity.User;
import com.bugtracker.bugtrack_backend.repository.NotificationRepository;
import com.bugtracker.bugtrack_backend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void sendNotification(Long userId, String message) {
        User user = userRepository.findById(userId).orElseThrow();
        Notification notification = new Notification();
        notification.setRecipient(user);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public List<NotificationDTO> getNotifications(Long userId) {
        return notificationRepository.findByRecipientIdOrderByTimestampDesc(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void markAsSeen(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + notificationId));
        notification.setSeen(true);
        notificationRepository.save(notification);
    }

    private NotificationDTO toDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getMessage(),
                notification.isSeen(),
                notification.getTimestamp()
        );
    }
}
