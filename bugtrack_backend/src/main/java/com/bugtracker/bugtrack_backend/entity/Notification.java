package com.bugtracker.bugtrack_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Notification {
    @Id @GeneratedValue
    private Long id;

    private String message;
    private boolean seen = false;
    private LocalDateTime timestamp;

    @ManyToOne
    private User recipient;
}
