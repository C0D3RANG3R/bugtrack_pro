package com.bugtracker.bugtrack_backend.service;

import com.bugtracker.bugtrack_backend.dto.*;
import com.bugtracker.bugtrack_backend.entity.User;
import com.bugtracker.bugtrack_backend.mapper.IssueMapper;
import com.bugtracker.bugtrack_backend.mapper.SubtaskMapper;
import com.bugtracker.bugtrack_backend.repository.IssueRepository;
import com.bugtracker.bugtrack_backend.repository.SubtaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IssueRepository issueRepo;
    private final SubtaskRepository subtaskRepo;
    private final IssueMapper issueMapper;

    public UserProfileDTO getCurrentUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        List<IssueResponseDTO> assignedIssues = issueRepo.findByAssigneeId(user.getId())
                .stream()
                .map(issueMapper::toDTO)
                .toList();

        List<SubtaskResponseDTO> assignedSubtasks = subtaskRepo.findByAssigneeId(user.getId())
                .stream()
                .map(SubtaskMapper::toDTO)
                .toList();

        return new UserProfileDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                assignedIssues,
                assignedSubtasks
        );
    }
}
