package com.pet.jiraliketracker.mapper;

import com.pet.jiraliketracker.dto.ProjectResponseDTO;
import com.pet.jiraliketracker.model.Project;
import com.pet.jiraliketracker.model.User;

public class ProjectMapper {
    public static ProjectResponseDTO toDto (Project project) {
        return new ProjectResponseDTO(
                project.getOwner().getEmail(), project.getMembers().stream()
                .map(User::getEmail)
                .toList(), project.getName());
    }
}