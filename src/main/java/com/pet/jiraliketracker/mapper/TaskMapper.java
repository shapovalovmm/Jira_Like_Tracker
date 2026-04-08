package com.pet.jiraliketracker.mapper;

import com.pet.jiraliketracker.dto.TaskResponseDTO;
import com.pet.jiraliketracker.model.Task;

public class TaskMapper {
    public static TaskResponseDTO toDto (Task task) {
        return new TaskResponseDTO(
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getAuthor().getEmail(),
                task.getAssignee().getEmail(),
                task.getProject().getName(),
                task.getStatus()
        );
    }
}
