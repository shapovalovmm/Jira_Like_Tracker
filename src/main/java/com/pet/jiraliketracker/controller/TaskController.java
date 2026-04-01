package com.pet.jiraliketracker.controller;

import com.pet.jiraliketracker.dto.*;
import com.pet.jiraliketracker.model.Project;
import com.pet.jiraliketracker.model.User;
import com.pet.jiraliketracker.service.AuthService;
import com.pet.jiraliketracker.service.ProjectService;
import com.pet.jiraliketracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    private final ProjectService projectService;
    UserService userService;
    AuthService authService;

    public TaskController(@Qualifier("userService")  UserService userService,
                          AuthService authService, ProjectService projectService) {
        this.userService = userService;
        this.authService = authService;
        this.projectService = projectService;
    }

    @PostMapping("/auth/register")
    public UserResponseDTO getRegister(@Valid @RequestBody RegisterRequestDTO request) {
        return authService.register(request);
    }

    @PostMapping("/auth/login")
    public UserResponseDTO getLogin(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    @GetMapping("/userProjects")
    public List<ProjectResponseDTO> getUserProjects(@RequestParam String email) {
        return userService.getUserProjects(email);
    }

    @PutMapping("/addMember")
    public ProjectResponseDTO addMember(@RequestBody AddMemberRequestDTO request) {
        return projectService.addMember(request);
    }

    @PostMapping("/createProject")
    public ProjectResponseDTO createProject(@RequestBody ProjectRequestDTO request) {
        return projectService.createProject(request);
    }

    // todo Один владелец - много проектов. Поиск проектов по названию, название - уникальный ключ.
}
