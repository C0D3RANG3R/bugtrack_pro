package com.bugtracker.bugtrack_backend.mapper;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import com.bugtracker.bugtrack_backend.dto.IssueRequestDTO;
import com.bugtracker.bugtrack_backend.dto.IssueResponseDTO;
import com.bugtracker.bugtrack_backend.entity.Issue;
import com.bugtracker.bugtrack_backend.entity.Project;
import com.bugtracker.bugtrack_backend.entity.User;
import com.bugtracker.bugtrack_backend.repository.ProjectRepository;
import com.bugtracker.bugtrack_backend.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class IssueMapper {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public Issue toEntity(final IssueRequestDTO dto, final User reporter) throws Exception {
        Issue issue = new Issue();
        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getDescription());
        issue.setType(dto.getType());
        issue.setPriority(dto.getPriority());
        issue.setStatus("OPEN");
        issue.setCreatedAt(LocalDate.now());
        issue.setUpdatedAt(LocalDate.now());
        issue.setReporter(reporter);

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new Exception("Project not found"));
        issue.setProject(project);

        User assignee = userRepository.findById(dto.getAssigneeId())
                .orElseThrow(() -> new Exception("Assignee not found"));
        issue.setAssignee(assignee);

        return issue;
    }

    public IssueResponseDTO toDTO(final Issue issue) {
        IssueResponseDTO dto = new IssueResponseDTO();
        dto.setId(issue.getId());
        dto.setTitle(issue.getTitle());
        dto.setDescription(issue.getDescription());
        dto.setType(issue.getType());
        dto.setPriority(issue.getPriority());
        dto.setStatus(issue.getStatus());
        dto.setReporterUsername(
            issue.getReporter() != null ? issue.getReporter().getUsername() : null
        );
        dto.setAssigneeUsername(
            issue.getAssignee() != null ? issue.getAssignee().getUsername() : null
        );
        dto.setProjectName(
            issue.getProject() != null ? issue.getProject().getName() : null
        );
        dto.setCreatedAt(issue.getCreatedAt());
        dto.setUpdatedAt(issue.getUpdatedAt());
        return dto;
    }
}
