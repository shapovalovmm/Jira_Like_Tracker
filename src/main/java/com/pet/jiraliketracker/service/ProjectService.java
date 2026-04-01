package com.pet.jiraliketracker.service;


import com.pet.jiraliketracker.dto.AddMemberRequestDTO;
import com.pet.jiraliketracker.dto.ProjectRequestDTO;
import com.pet.jiraliketracker.dto.ProjectResponseDTO;
import com.pet.jiraliketracker.model.Project;
import com.pet.jiraliketracker.model.User;
import com.pet.jiraliketracker.repository.ProjectRepository;
import com.pet.jiraliketracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final UserRepository userRepository;
    ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO request) {
        User owner = userRepository.findByEmail(request.getOwnerEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<User> members = userRepository.findAllByEmailIn(request.getMembersEmails());

        // У owner немає проєкту з таким ім'ям
        if (projectRepository.existsByOwnerAndName(owner, request.getName())) {
            throw new RuntimeException("Project with this name already exists");
        }
        // Не всі користувачі знайдені за списком emails
        if (members.size() != request.getMembersEmails().size()) {
            throw new RuntimeException("Some users not found");
        }
        // Owner`а ще немає в користувачах
        if (!members.contains(owner)) {
            members.add(owner);
        }
        Project project = new Project(owner, members, request.getName());
        projectRepository.save(project);
        return new ProjectResponseDTO(request.getOwnerEmail(), members.stream()
                .map(User::getEmail)
                .toList(),
                project.getName());
    }

    @Transactional
    public ProjectResponseDTO addMember(AddMemberRequestDTO request) {
        Project project = projectRepository.findById(request.getProject_id()).
                orElseThrow(() -> new RuntimeException("Project not found"));
        User newUser = userRepository.findById(request.getUser_id())
                .orElseThrow(()-> new RuntimeException("User not found"));

        // Щоб уникнути дублікатів
        if (!project.getMembers().contains(newUser)) {
            project.getMembers().add(newUser);
        }

        return new ProjectResponseDTO(project.getOwner().getEmail(), project.getMembers().stream()
                .map(User::getEmail)
                .toList(), project.getName());
    }

}
