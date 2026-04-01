package com.pet.jiraliketracker.service;

import com.pet.jiraliketracker.dto.LoginRequestDTO;
import com.pet.jiraliketracker.dto.ProjectResponseDTO;
import com.pet.jiraliketracker.dto.RegisterRequestDTO;
import com.pet.jiraliketracker.dto.UserResponseDTO;
import com.pet.jiraliketracker.model.Project;
import com.pet.jiraliketracker.model.User;
import com.pet.jiraliketracker.repository.ProjectRepository;
import com.pet.jiraliketracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CustomerUserDetailsService userDetailsService;
    private final ProjectRepository projectRepository;

    public UserService(@Qualifier("userRepository") UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       CustomerUserDetailsService userDetailsService,
                       ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.projectRepository = projectRepository;
    }

    public List<ProjectResponseDTO> getUserProjects(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Користувача з email '" + email + "' не знайдено в базі даних"));
        List<Project> projects = projectRepository.findByMembersContaining(user);
        List<ProjectResponseDTO> list = new ArrayList<>();

        for(Project project : projects) {
            list.add(new ProjectResponseDTO(project.getOwner().getEmail(), project.getMembers().stream()
                    .map(User::getEmail)
                    .toList(), project.getName()));
        }
        return list;
    }

}
