package com.bugtracker.bugtrack_backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bugtracker.bugtrack_backend.dto.SubtaskRequestDTO;
import com.bugtracker.bugtrack_backend.dto.SubtaskResponseDTO;
import com.bugtracker.bugtrack_backend.entity.Subtask;
import com.bugtracker.bugtrack_backend.mapper.SubtaskMapper;
import com.bugtracker.bugtrack_backend.repository.IssueRepository;
import com.bugtracker.bugtrack_backend.repository.SubtaskRepository;
import com.bugtracker.bugtrack_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubtaskService {
    private final SubtaskRepository subtaskRepo;
    private final IssueRepository issueRepo;
    private final UserRepository userRepo;

    public SubtaskResponseDTO create(SubtaskRequestDTO dto) {
        Subtask subtask = new Subtask();
        subtask.setTitle(dto.getTitle());
        subtask.setStatus("TODO");
        subtask.setEstimatedTime(dto.getEstimatedTime());
        subtask.setTimeSpent(0.0);
        subtask.setIssue(issueRepo.findById(dto.getIssueId()).orElseThrow());
        subtask.setAssignee(userRepo.findById(dto.getAssigneeId()).orElseThrow());
        subtask.setCreatedAt(LocalDate.now());
        subtask.setUpdatedAt(LocalDate.now());
        subtaskRepo.save(subtask);
        return SubtaskMapper.toDTO(subtask);
    }

    public List<SubtaskResponseDTO> getByIssue(Long issueId) {
        return subtaskRepo.findByIssueId(issueId).stream().map(SubtaskMapper::toDTO).collect(Collectors.toList());
    }

    public void updateStatus(Long id, String status) {
        Subtask subtask = subtaskRepo.findById(id).orElseThrow();
        subtask.setStatus(status);
        subtask.setUpdatedAt(LocalDate.now());
    }

    public void updateTimeSpent(Long id, Double hours) {
        Subtask subtask = subtaskRepo.findById(id).orElseThrow();
        subtask.setTimeSpent(hours);
        subtask.setUpdatedAt(LocalDate.now());
    }

    public List<SubtaskResponseDTO> getSubtasksByAssignee(Long assigneeId) {
    return subtaskRepo.findByAssigneeId(assigneeId)
                      .stream()
                      .map(SubtaskMapper::toDTO)
                      .collect(Collectors.toList());
    }

}
