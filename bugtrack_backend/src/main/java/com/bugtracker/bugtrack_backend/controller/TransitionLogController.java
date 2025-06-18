package com.bugtracker.bugtrack_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bugtracker.bugtrack_backend.entity.TransitionLog;
import com.bugtracker.bugtrack_backend.repository.TransitionLogRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transitions")
@RequiredArgsConstructor
public class TransitionLogController {

    private final TransitionLogRepository repo;

    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<TransitionLog>> getLogs(@PathVariable Long issueId) {
        return ResponseEntity.ok(repo.findByIssueIdOrderByChangedAtDesc(issueId));
    }
}
