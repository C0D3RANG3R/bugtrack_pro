package com.bugtracker.bugtrack_backend.controller;

import com.bugtracker.bugtrack_backend.dto.*;
import com.bugtracker.bugtrack_backend.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping
    public ResponseEntity<IssueResponseDTO> create(@RequestBody IssueRequestDTO dto) throws Exception {
        return ResponseEntity.ok(issueService.createIssue(dto));
    }

    @GetMapping
    public ResponseEntity<List<IssueResponseDTO>> getAll() {
        return ResponseEntity.ok(issueService.getAllIssues());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(issueService.getIssueById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        issueService.deleteIssue(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        System.out.println("User: " + SecurityContextHolder.getContext().getAuthentication());
        issueService.updateIssueStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long id,
                                                 @RequestParam Long authorId,
                                                 @RequestBody String content) {
        return ResponseEntity.ok(issueService.addComment(id, content, authorId));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(issueService.getComments(id));
    }

    @GetMapping("/project/{projectId}/kanban")
    public ResponseEntity<List<KanbanResponseDTO>> getKanbanBoard(@PathVariable Long projectId) {
        return ResponseEntity.ok(issueService.getKanbanBoardByProject(projectId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<IssueResponseDTO>> search(@RequestParam String query) {
        return ResponseEntity.ok(issueService.searchIssues(query));
    }
}
