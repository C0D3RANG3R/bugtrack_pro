package com.bugtracker.bugtrack_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action; // e.g., "Created Issue", "Updated Status", "Deleted Subtask"
    private LocalDateTime timestamp;

    @ManyToOne
    private User user;

    private Long referenceId; // issueId, subtaskId, etc.
}
