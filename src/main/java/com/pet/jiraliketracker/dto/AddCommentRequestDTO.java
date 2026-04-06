package com.pet.jiraliketracker.dto;

import lombok.Data;

@Data
public class AddCommentRequestDTO {
    private Long id;
    private String authorEmail;
    private String text;
}
