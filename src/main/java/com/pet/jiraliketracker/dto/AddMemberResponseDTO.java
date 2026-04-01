package com.pet.jiraliketracker.dto;


import lombok.Data;

@Data
public class AddMemberResponseDTO {
    String email;
    String name;

    AddMemberResponseDTO(String email, String name) {
        this.email = email;
        this.name = name;
    }

}
