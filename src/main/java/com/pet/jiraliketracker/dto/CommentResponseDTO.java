package com.pet.jiraliketracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDTO {
    private Long taskId;
    private String authorEmail;
    private String text;
}
