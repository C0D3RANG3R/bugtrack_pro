package com.bugtracker.bugtrack_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bugtracker.bugtrack_backend.entity.Subtask;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    List<Subtask> findByIssueId(Long issueId);
    List<Subtask> findByAssigneeId(Long assigneeId);
}
