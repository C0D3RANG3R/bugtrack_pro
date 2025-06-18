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

    private static final Path UPLOAD_DIR = Paths.get("uploads");

    public Attachment upload(MultipartFile file, Long issueId, Long subtaskId, Long userId) throws IOException {
        ensureUploadDirExists();

        String filename = generateUniqueFilename(file.getOriginalFilename());
        Path filePath = UPLOAD_DIR.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        Attachment attachment = buildAttachment(file, filePath, userId, issueId, subtaskId);

        attachmentRepo.save(attachment);

        sendUploadNotification(userId);
        logUploadActivity(userId, attachment);

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

    // --- Private helper methods ---

    private void ensureUploadDirExists() throws IOException {
        if (!Files.exists(UPLOAD_DIR)) {
            Files.createDirectories(UPLOAD_DIR);
        }
    }

    private String generateUniqueFilename(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }

    private Attachment buildAttachment(
            MultipartFile file,
            Path filePath,
            Long userId,
            Long issueId,
            Long subtaskId
    ) {
        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFilePath(filePath.toString());
        attachment.setFileType(file.getContentType());
        attachment.setUploadedAt(LocalDateTime.now());
        attachment.setUploadedBy(userRepo.findById(userId).orElseThrow());

        if (issueId != null) {
            Issue issue = issueRepo.findById(issueId).orElseThrow();
            attachment.setIssue(issue);
        }
        if (subtaskId != null) {
            Subtask subtask = subtaskRepo.findById(subtaskId).orElseThrow();
            attachment.setSubtask(subtask);
        }
        return attachment;
    }

    private void sendUploadNotification(Long userId) {
        notificationService.sendNotification(
                userId,
                "File uploaded by User ID: " + userId + " to Issue/Subtask"
        );
    }

    private void logUploadActivity(Long userId, Attachment attachment) {
        String targetType;
        Long targetId;

        if (attachment.getIssue() != null) {
            targetType = "issue: " + attachment.getIssue().getId();
            targetId = attachment.getIssue().getId();
        } else if (attachment.getSubtask() != null) {
            targetType = "subtask: " + attachment.getSubtask().getId();
            targetId = attachment.getSubtask().getId();
        } else {
            targetType = "unknown";
            targetId = null;
        }

        activityLogService.logAction(
                userId,
                "Uploaded file to " + targetType,
                targetId
        );
    }
}
