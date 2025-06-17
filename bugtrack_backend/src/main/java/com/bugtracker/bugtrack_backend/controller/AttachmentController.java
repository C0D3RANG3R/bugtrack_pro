package com.bugtracker.bugtrack_backend.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bugtracker.bugtrack_backend.entity.Attachment;
import com.bugtracker.bugtrack_backend.service.AttachmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService service;

    @PostMapping("/upload")
    public ResponseEntity<Attachment> upload(
            @RequestParam MultipartFile file,
            @RequestParam(required = false) Long issueId,
            @RequestParam(required = false) Long subtaskId,
            @RequestParam Long userId
    ) throws IOException {
        return ResponseEntity.ok(service.upload(file, issueId, subtaskId, userId));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) throws IOException {
        byte[] data = service.download(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + "\"")
                .body(data);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws IOException {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<Attachment>> getByIssue(@PathVariable Long issueId) {
        return ResponseEntity.ok(service.getByIssue(issueId));
    }

    @GetMapping("/subtask/{subtaskId}")
    public ResponseEntity<List<Attachment>> getBySubtask(@PathVariable Long subtaskId) {
        return ResponseEntity.ok(service.getBySubtask(subtaskId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attachment>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getByUser(userId));
    }
}
