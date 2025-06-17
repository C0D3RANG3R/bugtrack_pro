package com.bugtracker.bugtrack_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bugtracker.bugtrack_backend.dto.SubtaskRequestDTO;
import com.bugtracker.bugtrack_backend.dto.SubtaskResponseDTO;
import com.bugtracker.bugtrack_backend.service.SubtaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subtasks")
@RequiredArgsConstructor
public class SubtaskController {

    private final SubtaskService service;

    @PostMapping
    public ResponseEntity<SubtaskResponseDTO> create(@RequestBody SubtaskRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<SubtaskResponseDTO>> getByIssue(@PathVariable Long issueId) {
        return ResponseEntity.ok(service.getByIssue(issueId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        service.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/time")
    public ResponseEntity<Void> updateTime(@PathVariable Long id, @RequestParam Double hours) {
        service.updateTimeSpent(id, hours);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<List<SubtaskResponseDTO>> getByAssignee(@PathVariable Long assigneeId) {
        return ResponseEntity.ok(service.getSubtasksByAssignee(assigneeId));
    }
}
