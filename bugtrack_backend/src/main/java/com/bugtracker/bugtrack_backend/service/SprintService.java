package com.bugtracker.bugtrack_backend.service;

import com.bugtracker.bugtrack_backend.dto.SprintRequestDTO;
import com.bugtracker.bugtrack_backend.dto.SprintResponseDTO;
import com.bugtracker.bugtrack_backend.mapper.SprintMapper;
import com.bugtracker.bugtrack_backend.entity.Project;
import com.bugtracker.bugtrack_backend.entity.Sprint;
import com.bugtracker.bugtrack_backend.entity.Issue;
import com.bugtracker.bugtrack_backend.repository.IssueRepository;
import com.bugtracker.bugtrack_backend.repository.ProjectRepository;
import com.bugtracker.bugtrack_backend.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SprintService {

    private final SprintRepository sprintRepo;
    private final ProjectRepository projectRepo;
    private final IssueRepository issueRepo;

    public SprintResponseDTO create(SprintRequestDTO dto) {
        Project project = projectRepo.findById(dto.getProjectId()).orElseThrow();
        Sprint sprint = Sprint.builder()
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus())
                .project(project)
                .build();
        sprintRepo.save(sprint);
        return SprintMapper.toDTO(sprint);
    }

    public List<SprintResponseDTO> getByProject(Long projectId) {
        return sprintRepo.findByProjectId(projectId)
                .stream().map(SprintMapper::toDTO).collect(Collectors.toList());
    }

    public SprintResponseDTO getById(Long id) {
        return sprintRepo.findById(id).map(SprintMapper::toDTO).orElseThrow();
    }

    public void delete(Long id) {
        sprintRepo.deleteById(id);
    }

    public void updateStatus(Long id, String status) {
        Sprint sprint = sprintRepo.findById(id).orElseThrow();
        sprint.setStatus(status);
        sprintRepo.save(sprint);
    }

    public void assignIssue(Long sprintId, Long issueId) {
        Sprint sprint = sprintRepo.findById(sprintId).orElseThrow();
        Issue issue = issueRepo.findById(issueId).orElseThrow();
        issue.setSprint(sprint);
        issueRepo.save(issue);
    }

    public void removeIssue(Long issueId) {
        Issue issue = issueRepo.findById(issueId).orElseThrow();
        issue.setSprint(null);
        issueRepo.save(issue);
    }

    public List<Issue> getKanbanBySprint(Long sprintId) {
        return issueRepo.findAll().stream()
                .filter(issue -> issue.getSprint() != null && issue.getSprint().getId().equals(sprintId))
                .collect(Collectors.toList());
    }
}
