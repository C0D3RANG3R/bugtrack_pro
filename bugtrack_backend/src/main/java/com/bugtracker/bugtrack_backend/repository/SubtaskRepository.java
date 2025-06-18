package com.bugtracker.bugtrack_backend.repository;

import com.bugtracker.bugtrack_backend.entity.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    List<Subtask> findByIssueId(Long issueId);
    List<Subtask> findByAssigneeId(Long assigneeId);
}
