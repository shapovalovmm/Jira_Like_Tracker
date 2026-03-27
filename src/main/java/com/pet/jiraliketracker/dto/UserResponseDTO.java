package com.pet.jiraliketracker.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    public String email;
    public String username;

    public UserResponseDTO(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
