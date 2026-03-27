package com.pet.jiraliketracker.controller;

import com.pet.jiraliketracker.dto.LoginRequestDTO;
import com.pet.jiraliketracker.dto.RegisterRequestDTO;
import com.pet.jiraliketracker.dto.UserResponseDTO;
import com.pet.jiraliketracker.service.AuthService;
import com.pet.jiraliketracker.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
    UserService userService;
    AuthService authService;

    public TaskController(@Qualifier("userService")  UserService userService,
                          AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/auth/register")
    public UserResponseDTO getRegister(@RequestBody RegisterRequestDTO request) {
        return authService.register(request);
    }

    @PostMapping("/auth/login")
    public UserResponseDTO getLogin(@RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }
}
