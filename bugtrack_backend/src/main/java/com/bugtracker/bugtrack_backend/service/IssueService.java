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

    public IssueResponseDTO createIssue(IssueRequestDTO dto) throws Exception {
        User reporter = userRepo.findById(dto.getReporterId()).orElseThrow(() -> new RuntimeException("Reporter not found"));
        Issue issue = issueMapper.toEntity(dto, reporter);
        issueRepo.save(issue);
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
    }

    public CommentDTO addComment(Long issueId, String content, Long authorId) {
        Issue issue = issueRepo.findById(issueId).orElseThrow(() -> new RuntimeException("Issue not found"));
        User author = userRepo.findById(authorId).orElseThrow(() -> new RuntimeException("Author not found"));

        Comment comment = new Comment(null, content, LocalDateTime.now(), author, issue);
        commentRepo.save(comment);
        return commentMapper.toDTO(comment);
    }

    public List<CommentDTO> getComments(Long issueId) {
        return commentRepo.findByIssueIdOrderByTimestampAsc(issueId)
                .stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
