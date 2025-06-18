package com.bugtracker.bugtrack_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private boolean seen = false;
    private LocalDateTime timestamp;

    @ManyToOne
    private User recipient;
}
