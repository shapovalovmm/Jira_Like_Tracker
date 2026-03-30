package com.pet.jiraliketracker.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String email;
    private String username;
    private String token;

    public UserResponseDTO(String email, String username, String token) {
        this.email = email;
        this.username = username;
        this.token = token;
    }
}
