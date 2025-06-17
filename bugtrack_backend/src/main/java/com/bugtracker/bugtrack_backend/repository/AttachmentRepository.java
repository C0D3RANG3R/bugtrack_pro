package com.bugtracker.bugtrack_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bugtracker.bugtrack_backend.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByIssueId(Long issueId);
    List<Attachment> findBySubtaskId(Long subtaskId);
    List<Attachment> findByUploadedById(Long uploadedById);
}
