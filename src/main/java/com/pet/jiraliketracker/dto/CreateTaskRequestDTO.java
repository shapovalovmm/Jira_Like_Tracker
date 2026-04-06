package com.pet.jiraliketracker.dto;

import lombok.Data;

@Data
public class CreateTaskRequestDTO {
    private String title;
    private String description;
    private String authorEmail;
    private String assigneeEmail;
    private Long projectId;
}
