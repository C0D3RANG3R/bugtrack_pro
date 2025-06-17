package com.bugtracker.bugtrack_backend.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Sprint {
    @Id 
    @GeneratedValue
    private Long id;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // NOT_STARTED, ACTIVE, COMPLETED

    @ManyToOne
    private Project project;

    @OneToMany(mappedBy = "sprint")
    private List<Issue> issues;
}
