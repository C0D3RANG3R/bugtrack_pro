package com.bugtracker.bugtrack_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subtask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String status; // TODO, IN_PROGRESS, DONE

    private Double estimatedTime; // in hours
    private Double timeSpent;

    @ManyToOne
    private Issue issue;

    @ManyToOne
    private User assignee;

    private LocalDate createdAt;
    private LocalDate updatedAt;
}
