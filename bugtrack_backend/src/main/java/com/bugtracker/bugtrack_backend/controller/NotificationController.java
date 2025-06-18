package com.bugtracker.bugtrack_backend.controller;

import com.bugtracker.bugtrack_backend.dto.NotificationDTO;
import com.bugtracker.bugtrack_backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{id}/seen")
    public ResponseEntity<Void> markNotificationAsSeen(@PathVariable Long id) {
        notificationService.markAsSeen(id);
        return ResponseEntity.ok().build();
    }
}
