package com.bugtracker.bugtrack_backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
