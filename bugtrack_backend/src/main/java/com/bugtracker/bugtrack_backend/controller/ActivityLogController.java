package com.bugtracker.bugtrack_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bugtracker.bugtrack_backend.entity.ActivityLog;
import com.bugtracker.bugtrack_backend.service.ActivityLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/activity-logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService service;

    @GetMapping
    public ResponseEntity<List<ActivityLog>> getAllLogs() {
        return ResponseEntity.ok(service.getAllLogs());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityLog>> getLogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getLogsByUser(userId));
    }
}
