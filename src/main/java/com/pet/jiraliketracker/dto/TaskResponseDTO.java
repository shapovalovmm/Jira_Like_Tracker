package com.pet.jiraliketracker.dto;

import com.pet.jiraliketracker.model.Project;
import com.pet.jiraliketracker.model.Status;
import com.pet.jiraliketracker.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskResponseDTO {
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String authorEmail;
    private String assigneeEmail;
    private String projectName;
    private Status status;
}
