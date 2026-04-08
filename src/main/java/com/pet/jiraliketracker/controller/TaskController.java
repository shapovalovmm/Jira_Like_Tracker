package com.pet.jiraliketracker.controller;

import com.pet.jiraliketracker.dto.*;

import com.pet.jiraliketracker.model.Status;
import com.pet.jiraliketracker.service.AuthService;
import com.pet.jiraliketracker.service.ProjectService;
import com.pet.jiraliketracker.service.TaskService;
import com.pet.jiraliketracker.service.UserService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class TaskController {
    private final ProjectService projectService;
    private final UserService userService;
    private final AuthService authService;
    private final TaskService taskService;

    public TaskController(UserService userService,
                          AuthService authService,
                          ProjectService projectService,
                          TaskService taskService) {
        this.userService = userService;
        this.authService = authService;
        this.projectService = projectService;
        this.taskService = taskService;
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

    @PostMapping("/createTask")
    public TaskResponseDTO createTask(@RequestBody CreateTaskRequestDTO request) {
        return taskService.createTask(request);
    }

    @PutMapping("/changeStatus")
    public TaskResponseDTO changeStatus(@RequestBody ChangeStatusRequestDTO request) {
        return taskService.changeStatus(request);
    }

    @PutMapping("/assign")
    public TaskResponseDTO assign(@RequestBody AssignRequestDTO request) {
        return taskService.assign(request);
    }

    @GetMapping("/getTasks")
    public Page<TaskResponseDTO> getTasks(@RequestParam Long id, Pageable pageable) {
        return taskService.getTasks(id, pageable);
    }

    @GetMapping("/getTasksByPriority")
    public Page<TaskResponseDTO> getTasksByPriority(@RequestParam Long id, @RequestParam int priority, Pageable pageable) {
        return taskService.getTasksByPriority(id, priority, pageable);
    }

    @GetMapping("/getTasksByStatus")
    public Page<TaskResponseDTO> getTasksStatus(@RequestParam Long id, @RequestParam Status status, Pageable pageable) {
        return taskService.getTasksByStatus(id, status, pageable);
    }

    @PostMapping("/addComment")
    public CommentResponseDTO addComment(@RequestBody AddCommentRequestDTO request) {
        return taskService.addComment(request);
    }

    @GetMapping("/getTaskComments")
    public List<CommentResponseDTO> getTaskComments(@RequestParam Long id) {
        return taskService.getTaskComments(id);
    }
}

