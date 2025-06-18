package com.bugtracker.bugtrack_backend.repository;

import com.bugtracker.bugtrack_backend.entity.TransitionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransitionLogRepository extends JpaRepository<TransitionLog, Long> {
    List<TransitionLog> findByIssueIdOrderByChangedAtDesc(Long issueId);
}
