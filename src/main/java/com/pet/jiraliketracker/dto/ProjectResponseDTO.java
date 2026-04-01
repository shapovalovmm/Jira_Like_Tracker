package com.pet.jiraliketracker.dto;

import com.pet.jiraliketracker.model.User;
import lombok.Data;

import java.util.List;

@Data
public class ProjectResponseDTO {
    private String ownerEmail;
    private List<String> membersEmails;
    private String name;

    public ProjectResponseDTO(String ownerEmail, List<String> membersEmails, String name) {
        this.ownerEmail = ownerEmail;
        this.membersEmails = membersEmails;
        this.name = name;
    }

    public ProjectResponseDTO() {

    }
}
