package com.bugtracker.bugtrack_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

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
