package com.bugtracker.bugtrack_backend.controller;

import com.bugtracker.bugtrack_backend.entity.TransitionLog;
import com.bugtracker.bugtrack_backend.repository.TransitionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transitions")
@RequiredArgsConstructor
public class TransitionLogController {

    private final TransitionLogRepository transitionLogRepository;

    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<TransitionLog>> getLogsByIssueId(@PathVariable Long issueId) {
        List<TransitionLog> logs = transitionLogRepository.findByIssueIdOrderByChangedAtDesc(issueId);
        return ResponseEntity.ok(logs);
    }
}
