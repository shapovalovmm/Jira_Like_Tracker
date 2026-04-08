package com.pet.jiraliketracker.mapper;

import com.pet.jiraliketracker.dto.CommentResponseDTO;
import com.pet.jiraliketracker.model.Comment;

public class CommentMapper {
    public static CommentResponseDTO toDto (Comment comment) {
        return new CommentResponseDTO(
                comment.getTask().getId(),
                comment.getUser().getEmail(),
                comment.getText());
    }
}
