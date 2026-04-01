package com.pet.jiraliketracker.dto;

import com.pet.jiraliketracker.model.User;
import lombok.Data;

import java.util.List;

@Data
public class ProjectRequestDTO {
    private String ownerEmail;
    private List<String> membersEmails;
    private String name;
}
