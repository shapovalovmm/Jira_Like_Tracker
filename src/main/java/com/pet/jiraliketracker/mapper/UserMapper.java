package com.pet.jiraliketracker.mapper;

import com.pet.jiraliketracker.dto.UserResponseDTO;
import com.pet.jiraliketracker.model.User;

public class UserMapper {
    public static UserResponseDTO toDto (User user, String token) {
        return new UserResponseDTO(
                user.getEmail(),
                user.getUsername(),
                token
        );

    }
}

