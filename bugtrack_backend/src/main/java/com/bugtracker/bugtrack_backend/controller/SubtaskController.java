package com.bugtracker.bugtrack_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<SubtaskResponseDTO> createSubtask(@RequestBody SubtaskRequestDTO dto) {
        SubtaskResponseDTO response = service.create(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<SubtaskResponseDTO>> getSubtasksByIssue(@PathVariable Long issueId) {
        List<SubtaskResponseDTO> subtasks = service.getByIssue(issueId);
        return ResponseEntity.ok(subtasks);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateSubtaskStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        service.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/time")
    public ResponseEntity<Void> updateSubtaskTimeSpent(
            @PathVariable Long id,
            @RequestParam Double hours) {
        service.updateTimeSpent(id, hours);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<List<SubtaskResponseDTO>> getSubtasksByAssignee(@PathVariable Long assigneeId) {
        List<SubtaskResponseDTO> subtasks = service.getSubtasksByAssignee(assigneeId);
        return ResponseEntity.ok(subtasks);
    }
}
