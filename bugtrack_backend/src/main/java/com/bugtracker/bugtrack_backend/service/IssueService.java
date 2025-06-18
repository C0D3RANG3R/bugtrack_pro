package com.bugtracker.bugtrack_backend.service;

import com.bugtracker.bugtrack_backend.dto.*;
import com.bugtracker.bugtrack_backend.entity.Comment;
import com.bugtracker.bugtrack_backend.entity.Issue;
import com.bugtracker.bugtrack_backend.entity.TransitionLog;
import com.bugtracker.bugtrack_backend.entity.User;
import com.bugtracker.bugtrack_backend.mapper.CommentMapper;
import com.bugtracker.bugtrack_backend.mapper.IssueMapper;
import com.bugtracker.bugtrack_backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final TransitionLogRepository transitionLogRepo;
    private final EmailService emailService;

    public IssueResponseDTO createIssue(IssueRequestDTO dto) throws Exception {
        User reporter = userRepo.findById(dto.getReporterId())
            .orElseThrow(() -> new RuntimeException("Reporter not found"));
        Issue issue = issueMapper.toEntity(dto, reporter);
        issueRepo.save(issue);

        notifyAssignee(issue);
        logIssueCreation(dto.getReporterId(), issue);
        sendIssueAssignmentEmail(issue);

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
        Issue issue = issueRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Issue not found"));
        String oldStatus = issue.getStatus();

        issue.setStatus(status);
        issue.setUpdatedAt(LocalDate.now());
        issueRepo.save(issue);

        User user = getCurrentUser();
        TransitionLog log = new TransitionLog(
            null, issue, oldStatus, status, user, LocalDateTime.now()
        );
        transitionLogRepo.save(log);
    }

    public CommentDTO addComment(Long issueId, String content, Long authorId) {
        Issue issue = issueRepo.findById(issueId)
            .orElseThrow(() -> new RuntimeException("Issue not found"));
        User author = userRepo.findById(authorId)
            .orElseThrow(() -> new RuntimeException("Author not found"));

        Comment comment = new Comment(
            null, content, LocalDateTime.now(), author, issue
        );
        commentRepo.save(comment);

        notifyComment(author, issue);
        logComment(authorId, issue);
        sendCommentEmail(issue, author, content);

        return commentMapper.toDTO(comment);
    }

    public List<CommentDTO> getComments(Long issueId) {
        return commentRepo.findByIssueIdOrderByTimestampAsc(issueId)
            .stream()
            .map(commentMapper::toDTO)
            .collect(Collectors.toList());
    }

    public List<KanbanResponseDTO> getKanbanBoardByProject(Long projectId) {
        List<String> statuses = List.of("OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED");
        return statuses.stream()
            .map(status -> {
                List<Issue> issues = issueRepo.findByProjectIdAndStatus(projectId, status);
                List<IssueResponseDTO> dtos = issues.stream()
                    .map(issueMapper::toDTO)
                    .collect(Collectors.toList());
                return new KanbanResponseDTO(status, dtos);
            })
            .collect(Collectors.toList());
    }

    public List<IssueResponseDTO> searchIssues(String query) {
        return issueRepo.search(query)
            .stream()
            .map(issueMapper::toDTO)
            .collect(Collectors.toList());
    }

    public List<IssueResponseDTO> filterIssues(
        String status, String priority, Long assigneeId, Long projectId
    ) {
        List<Issue> issues = issueRepo.findAll();

        if (status != null) {
            issues = issues.stream()
                .filter(i -> i.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
        }
        if (priority != null) {
            issues = issues.stream()
                .filter(i -> i.getPriority().equalsIgnoreCase(priority))
                .collect(Collectors.toList());
        }
        if (assigneeId != null) {
            issues = issues.stream()
                .filter(i -> i.getAssignee().getId().equals(assigneeId))
                .collect(Collectors.toList());
        }
        if (projectId != null) {
            issues = issues.stream()
                .filter(i -> i.getProject().getId().equals(projectId))
                .collect(Collectors.toList());
        }

        return issues.stream()
            .map(issueMapper::toDTO)
            .collect(Collectors.toList());
    }

    // --- Private helper methods ---

    private void notifyAssignee(Issue issue) {
        notificationService.sendNotification(
            issue.getAssignee().getId(),
            "Issue assigned to: " + issue.getAssignee().getUsername()
        );
    }

    private void logIssueCreation(Long reporterId, Issue issue) {
        activityLogService.logAction(
            reporterId,
            "Created issue: " + issue.getTitle(),
            issue.getId()
        );
    }

    private void sendIssueAssignmentEmail(Issue issue) {
        emailService.send(
            issue.getAssignee().getEmail(),
            "📝 New Issue Assigned: " + issue.getTitle(),
            "You have been assigned a new issue in project: " + issue.getProject().getName() +
                "\n\nDescription: " + issue.getDescription()
        );
    }

    private void notifyComment(User author, Issue issue) {
        notificationService.sendNotification(
            author.getId(),
            "New comment by " + author.getUsername() + " on issue: " + issue.getTitle()
        );
    }

    private void logComment(Long authorId, Issue issue) {
        activityLogService.logAction(
            authorId,
            "Added comment to issue: " + issue.getTitle(),
            issue.getId()
        );
    }

    private void sendCommentEmail(Issue issue, User author, String content) {
        emailService.send(
            issue.getReporter().getEmail(),
            "New Comment on Issue: " + issue.getTitle(),
            author.getUsername() + " added a new comment: \n\n" + content
        );
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
}
