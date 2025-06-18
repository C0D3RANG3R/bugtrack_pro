package com.bugtracker.bugtrack_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bugtracker.bugtrack_backend.dto.NotificationDTO;
import com.bugtracker.bugtrack_backend.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDTO>> getAll(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getNotifications(userId));
    }

    @PatchMapping("/{id}/seen")
    public ResponseEntity<Void> markAsSeen(@PathVariable Long id) {
        service.markAsSeen(id);
        return ResponseEntity.ok().build();
    }
}
