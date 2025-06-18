package com.bugtracker.bugtrack_backend.service;

import com.bugtracker.bugtrack_backend.dto.*;
import com.bugtracker.bugtrack_backend.entity.Comment;
import com.bugtracker.bugtrack_backend.entity.Issue;
import com.bugtracker.bugtrack_backend.entity.User;
import com.bugtracker.bugtrack_backend.mapper.CommentMapper;
import com.bugtracker.bugtrack_backend.mapper.IssueMapper;
import com.bugtracker.bugtrack_backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepo;
    private final UserRepository userRepo;
    private final CommentRepository commentRepo;
    private final IssueMapper issueMapper;
    private final CommentMapper commentMapper;
    private final NotificationService notificationService;
    private final ActivityLogService activityLogService;

    public IssueResponseDTO createIssue(IssueRequestDTO dto) throws Exception {
        User reporter = userRepo.findById(dto.getReporterId()).orElseThrow(() -> new RuntimeException("Reporter not found"));
        Issue issue = issueMapper.toEntity(dto, reporter);
        issueRepo.save(issue);
        notificationService.sendNotification(issue.getAssignee().getId(), "Issue assigned to: " + issue.getAssignee().getUsername());
        activityLogService.logAction(
            dto.getReporterId(),
            "Created issue: " + issue.getTitle(),
            issue.getId());
        return issueMapper.toDTO(issue);
    }

    public List<IssueResponseDTO> getAllIssues() {
        return issueRepo.findAll()
                .stream()
                .map(issueMapper::toDTO)
                .collect(Collectors.toList());
    }

    public IssueResponseDTO getIssueById(Long id) {
        return issueRepo.findById(id)
                .map(issueMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Issue not found"));
    }

    public void deleteIssue(Long id) {
        if (!issueRepo.existsById(id)) {
            throw new RuntimeException("Issue not found");
        }
        issueRepo.deleteById(id);
    }

    @Transactional
    public void updateIssueStatus(Long id, String status) {
        Issue issue = issueRepo.findById(id).orElseThrow(() -> new RuntimeException("Issue not found"));
        issue.setStatus(status);
        issue.setUpdatedAt(LocalDate.now());
        activityLogService.logAction(
            issue.getAssignee().getId(),
            "Updated status of issue to: " + status,
            issue.getId());
    }

    public CommentDTO addComment(Long issueId, String content, Long authorId) {
        Issue issue = issueRepo.findById(issueId).orElseThrow(() -> new RuntimeException("Issue not found"));
        User author = userRepo.findById(authorId).orElseThrow(() -> new RuntimeException("Author not found"));

        Comment comment = new Comment(null, content, LocalDateTime.now(), author, issue);
        commentRepo.save(comment);
        notificationService.sendNotification(authorId, "New comment by " + author.getUsername() + " on issue: " + issue.getTitle());
        activityLogService.logAction(
            authorId,
            "Added comment to issue: " + issue.getTitle(),
            issue.getId());
        return commentMapper.toDTO(comment);
    }

    public List<CommentDTO> getComments(Long issueId) {
        return commentRepo.findByIssueIdOrderByTimestampAsc(issueId)
                .stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
