package com.bugtracker.bugtrack_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bugtracker.bugtrack_backend.entity.TransitionLog;

@Repository
public interface TransitionLogRepository extends JpaRepository<TransitionLog, Long> {
    List<TransitionLog> findByIssueIdOrderByChangedAtDesc(Long issueId);
}
