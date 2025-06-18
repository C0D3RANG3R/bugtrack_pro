package com.bugtracker.bugtrack_backend.controller;

import com.bugtracker.bugtrack_backend.dto.SprintRequestDTO;
import com.bugtracker.bugtrack_backend.dto.SprintResponseDTO;
import com.bugtracker.bugtrack_backend.entity.Issue;
import com.bugtracker.bugtrack_backend.service.SprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sprints")
@RequiredArgsConstructor
public class SprintController {

    private final SprintService sprintService;

    @PostMapping
    public ResponseEntity<SprintResponseDTO> create(@RequestBody SprintRequestDTO dto) {
        SprintResponseDTO response = sprintService.create(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<SprintResponseDTO>> getByProject(@PathVariable Long projectId) {
        List<SprintResponseDTO> sprints = sprintService.getByProject(projectId);
        return ResponseEntity.ok(sprints);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintResponseDTO> getById(@PathVariable Long id) {
        SprintResponseDTO sprint = sprintService.getById(id);
        return ResponseEntity.ok(sprint);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sprintService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        sprintService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{sprintId}/assign/{issueId}")
    public ResponseEntity<Void> assignIssue(
            @PathVariable Long sprintId,
            @PathVariable Long issueId
    ) {
        sprintService.assignIssue(sprintId, issueId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/removeIssue/{issueId}")
    public ResponseEntity<Void> removeIssue(@PathVariable Long issueId) {
        sprintService.removeIssue(issueId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sprintId}/kanban")
    public ResponseEntity<List<Issue>> kanban(@PathVariable Long sprintId) {
        List<Issue> issues = sprintService.getKanbanBySprint(sprintId);
        return ResponseEntity.ok(issues);
    }
}
