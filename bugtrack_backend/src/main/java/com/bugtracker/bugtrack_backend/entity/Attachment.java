package com.bugtracker.bugtrack_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private String fileType;
    private LocalDateTime uploadedAt;

    @ManyToOne
    private Issue issue;

    @ManyToOne
    private Subtask subtask;

    @ManyToOne
    private User uploadedBy;
}
