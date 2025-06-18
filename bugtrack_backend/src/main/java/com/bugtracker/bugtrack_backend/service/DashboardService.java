package com.bugtracker.bugtrack_backend.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bugtracker.bugtrack_backend.dto.DashboardDTO;
import com.bugtracker.bugtrack_backend.repository.IssueRepository;
import com.bugtracker.bugtrack_backend.repository.ProjectRepository;
import com.bugtracker.bugtrack_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepo;
    private final ProjectRepository projectRepo;
    private final IssueRepository issueRepo;

    public DashboardDTO getDashboardStats() {
    long totalUsers = userRepo.count();
    long totalProjects = projectRepo.count();
    long totalIssues = issueRepo.count();

    Map<String, Long> statusMap = new HashMap<>();
    for (Object[] row : issueRepo.countByStatusGroupRaw()) {
        statusMap.put((String) row[0], (Long) row[1]);
    }

    Map<String, Long> priorityMap = new HashMap<>();
    for (Object[] row : issueRepo.countByPriorityGroupRaw()) {
        priorityMap.put((String) row[0], (Long) row[1]);
    }

    return new DashboardDTO(totalUsers, totalProjects, totalIssues, statusMap, priorityMap);
}

}
