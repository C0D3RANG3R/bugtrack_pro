package com.bugtracker.bugtrack_backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bugtracker.bugtrack_backend.entity.Attachment;
import com.bugtracker.bugtrack_backend.entity.Issue;
import com.bugtracker.bugtrack_backend.entity.Subtask;
import com.bugtracker.bugtrack_backend.repository.AttachmentRepository;
import com.bugtracker.bugtrack_backend.repository.IssueRepository;
import com.bugtracker.bugtrack_backend.repository.SubtaskRepository;
import com.bugtracker.bugtrack_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository attachmentRepo;
    private final IssueRepository issueRepo;
    private final SubtaskRepository subtaskRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;
    private final ActivityLogService activityLogService;

    private final Path uploadDir = Paths.get("uploads");

    public Attachment upload(MultipartFile file, Long issueId, Long subtaskId, Long userId) throws IOException {
        if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFilePath(filePath.toString());
        attachment.setFileType(file.getContentType());
        attachment.setUploadedAt(LocalDateTime.now());
        attachment.setUploadedBy(userRepo.findById(userId).orElseThrow());

        Issue issue = null;
        Subtask subtask = null;
        if (issueId != null) {
            issue = issueRepo.findById(issueId).orElseThrow();
            attachment.setIssue(issue);
        }
        if (subtaskId != null) {
            subtask = subtaskRepo.findById(subtaskId).orElseThrow();
            attachment.setSubtask(subtask);
        }

        attachmentRepo.save(attachment);
        notificationService.sendNotification(userId, "File uploaded by User ID: " + userId + " to Issue/Subtask");
        activityLogService.logAction(
            userId,
            "Uploaded file to " + (issue != null ? "issue: " + (issue).getId() : "subtask: " + (subtask).getId()),
            issue != null ? (issue).getId() : (subtask).getId()
        );
        return attachment;

    }

    public byte[] download(Long id) throws IOException {
        Attachment attachment = attachmentRepo.findById(id).orElseThrow();
        return Files.readAllBytes(Paths.get(attachment.getFilePath()));
    }

    public void delete(Long id) throws IOException {
        Attachment attachment = attachmentRepo.findById(id).orElseThrow();
        Files.deleteIfExists(Paths.get(attachment.getFilePath()));
        attachmentRepo.delete(attachment);
    }

    public List<Attachment> getByIssue(Long issueId) {
        return attachmentRepo.findByIssueId(issueId);
    }

    public List<Attachment> getBySubtask(Long subtaskId) {
        return attachmentRepo.findBySubtaskId(subtaskId);
    }

    public List<Attachment> getByUser(Long uploadedById) {
        return attachmentRepo.findByUploadedById(uploadedById);
    }

}
