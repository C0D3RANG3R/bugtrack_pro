package com.bugtracker.bugtrack_backend.controller;

import com.bugtracker.bugtrack_backend.entity.Attachment;
import com.bugtracker.bugtrack_backend.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
        Attachment attachment = service.upload(file, issueId, subtaskId, userId);
        return ResponseEntity.ok(attachment);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) throws IOException {
        byte[] data = service.download(id);
        String contentDisposition = String.format("attachment; filename=\"%d\"", id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(data);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) throws IOException {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<Attachment>> getByIssue(@PathVariable Long issueId) {
        List<Attachment> attachments = service.getByIssue(issueId);
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/subtask/{subtaskId}")
    public ResponseEntity<List<Attachment>> getBySubtask(@PathVariable Long subtaskId) {
        List<Attachment> attachments = service.getBySubtask(subtaskId);
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Attachment>> getByUser(@PathVariable Long userId) {
        List<Attachment> attachments = service.getByUser(userId);
        return ResponseEntity.ok(attachments);
    }
}
