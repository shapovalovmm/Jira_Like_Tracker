package com.pet.jiraliketracker.service;


import com.pet.jiraliketracker.dto.AddMemberRequestDTO;
import com.pet.jiraliketracker.dto.ProjectRequestDTO;
import com.pet.jiraliketracker.dto.ProjectResponseDTO;
import com.pet.jiraliketracker.mapper.ProjectMapper;
import com.pet.jiraliketracker.model.Project;
import com.pet.jiraliketracker.model.User;
import com.pet.jiraliketracker.repository.ProjectRepository;
import com.pet.jiraliketracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO request) {
        log.info("Creating project with name: {}", request.getName());

        User owner = userRepository.findByEmail(request.getOwnerEmail())
                .orElseThrow(() -> {
                    log.warn("Owner not found with email: {}", request.getOwnerEmail());
                    return new RuntimeException("User not found");
                });

        List<User> members = userRepository.findAllByEmailIn(request.getMembersEmails());

        // У owner немає проєкту з таким ім'ям
        if (projectRepository.existsByOwnerAndName(owner, request.getName())) {
            log.warn("User {} tried to create duplicate project with name {}",
                    owner.getEmail(), request.getName());
            throw new RuntimeException("Project with this name already exists");
        }

        // Не всі користувачі знайдені
        if (members.size() != request.getMembersEmails().size()) {
            log.warn("Not all users found for project creation. Requested: {}, Found: {}",
                    request.getMembersEmails().size(), members.size());
            throw new RuntimeException("Some users not found");
        }

        // Додаємо owner якщо його нема
        if (!members.contains(owner)) {
            members.add(owner);
        }

        Project project = new Project(owner, members, request.getName());
        projectRepository.save(project);

        log.info("Project created successfully. id={}, owner={}",
                project.getId(), owner.getEmail());

        return ProjectMapper.toDto(project);
    }

    @Transactional
    public ProjectResponseDTO addMember(AddMemberRequestDTO request) {
        log.info("Adding user {} to project {}",
                request.getUser_id(), request.getProject_id());

        Project project = projectRepository.findById(request.getProject_id())
                .orElseThrow(() -> {
                    log.warn("Project not found with id: {}", request.getProject_id());
                    return new RuntimeException("Project not found");
                });

        User newUser = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", request.getUser_id());
                    return new RuntimeException("User not found");
                });

        // Щоб уникнути дублікатів
        if (!project.getMembers().contains(newUser)) {
            project.getMembers().add(newUser);

            log.info("User {} added to project {}",
                    newUser.getEmail(), project.getId());
        } else {
            log.warn("User {} already exists in project {}",
                    newUser.getEmail(), project.getId());
        }

        return ProjectMapper.toDto(project);
    }
}