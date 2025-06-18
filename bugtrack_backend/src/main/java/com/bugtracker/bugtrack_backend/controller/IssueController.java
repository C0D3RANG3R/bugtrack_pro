package com.bugtracker.bugtrack_backend.controller;

import com.bugtracker.bugtrack_backend.dto.*;
import com.bugtracker.bugtrack_backend.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping
    public ResponseEntity<IssueResponseDTO> create(@RequestBody IssueRequestDTO dto) throws Exception {
        IssueResponseDTO response = issueService.createIssue(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<IssueResponseDTO>> getAll() {
        List<IssueResponseDTO> issues = issueService.getAllIssues();
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseDTO> getById(@PathVariable Long id) {
        IssueResponseDTO issue = issueService.getIssueById(id);
        return ResponseEntity.ok(issue);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        issueService.deleteIssue(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        issueService.updateIssueStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long id,
            @RequestParam Long authorId,
            @RequestBody String content) {
        CommentDTO comment = issueService.addComment(id, content, authorId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long id) {
        List<CommentDTO> comments = issueService.getComments(id);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/project/{projectId}/kanban")
    public ResponseEntity<List<KanbanResponseDTO>> getKanbanBoard(@PathVariable Long projectId) {
        List<KanbanResponseDTO> kanban = issueService.getKanbanBoardByProject(projectId);
        return ResponseEntity.ok(kanban);
    }

    @GetMapping("/search")
    public ResponseEntity<List<IssueResponseDTO>> search(@RequestParam String query) {
        List<IssueResponseDTO> results = issueService.searchIssues(query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<IssueResponseDTO>> filterIssues(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) Long projectId) {
        List<IssueResponseDTO> filtered = issueService.filterIssues(status, priority, assigneeId, projectId);
        return ResponseEntity.ok(filtered);
    }
}
