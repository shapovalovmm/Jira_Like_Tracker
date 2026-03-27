package com.pet.jiraliketracker.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    public String email;
    public String password;
    public String username;
}
