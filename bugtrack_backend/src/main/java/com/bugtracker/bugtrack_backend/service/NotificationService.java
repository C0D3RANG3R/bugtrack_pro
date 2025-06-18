package com.bugtracker.bugtrack_backend.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import com.bugtracker.bugtrack_backend.repository.NotificationRepository;
import com.bugtracker.bugtrack_backend.repository.UserRepository;
import com.bugtracker.bugtrack_backend.entity.Notification;
import com.bugtracker.bugtrack_backend.entity.User;
import com.bugtracker.bugtrack_backend.dto.NotificationDTO;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;
    private final UserRepository userRepo;

    public void sendNotification(Long userId, String message) {
        User user = userRepo.findById(userId).orElseThrow();
        Notification notification = new Notification();
        notification.setRecipient(user);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        repo.save(notification);
    }

    public List<NotificationDTO> getNotifications(Long userId) {
        return repo.findByRecipientIdOrderByTimestampDesc(userId)
                   .stream()
                   .map(this::toDTO)
                   .collect(Collectors.toList());
    }

    public void markAsSeen(Long id) {
        Notification notif = repo.findById(id).orElseThrow();
        notif.setSeen(true);
    }

    private NotificationDTO toDTO(Notification n) {
        return new NotificationDTO(n.getId(), n.getMessage(), n.isSeen(), n.getTimestamp());
    }
}
