package com.pet.jiraliketracker.controller;

import com.pet.jiraliketracker.dto.LoginRequestDTO;
import com.pet.jiraliketracker.dto.RegisterRequestDTO;
import com.pet.jiraliketracker.dto.UserResponseDTO;
import com.pet.jiraliketracker.model.User;
import com.pet.jiraliketracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    UserRepository userRepository;

    public TaskController(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public UserResponseDTO getRegister(@RequestBody RegisterRequestDTO request) {
        userRepository.save(new User(request.username, request.email, request.password, "CLIENT"));
        return new UserResponseDTO(request.email, request.username);
    }

    @PostMapping("/login")
    public UserResponseDTO getLogin(@RequestBody LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email).orElseThrow(() -> new RuntimeException("User not found"));
        if(user.getPassword().equals(request.password)) {
            return new UserResponseDTO(request.email, userRepository.findByEmail(request.email).get().getUsername());
        } else return new UserResponseDTO("failed", "failed");
    }
}
