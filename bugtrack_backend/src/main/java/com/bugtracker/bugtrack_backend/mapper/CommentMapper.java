package com.bugtracker.bugtrack_backend.mapper;

import org.springframework.stereotype.Component;

import com.bugtracker.bugtrack_backend.dto.CommentDTO;
import com.bugtracker.bugtrack_backend.entity.Comment;

@Component
public class CommentMapper {
    public CommentDTO toDTO(final Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setTimestamp(comment.getTimestamp());
        dto.setAuthorUsername(
            comment.getAuthor() != null ? comment.getAuthor().getUsername() : null
        );
        return dto;
    }
}
