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

        Map<String, Long> statusMap = mapGroupResult(issueRepo.countByStatusGroupRaw());
        Map<String, Long> priorityMap = mapGroupResult(issueRepo.countByPriorityGroupRaw());

        return new DashboardDTO(
            totalUsers,
            totalProjects,
            totalIssues,
            statusMap,
            priorityMap
        );
    }

    private Map<String, Long> mapGroupResult(Iterable<Object[]> groupResult) {
        Map<String, Long> resultMap = new HashMap<>();
        for (Object[] row : groupResult) {
            resultMap.put((String) row[0], (Long) row[1]);
        }
        return resultMap;
    }
}
