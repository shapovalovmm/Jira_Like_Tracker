package com.pet.jiraliketracker.service;

import com.pet.jiraliketracker.dto.LoginRequestDTO;
import com.pet.jiraliketracker.dto.RegisterRequestDTO;
import com.pet.jiraliketracker.dto.UserResponseDTO;
import com.pet.jiraliketracker.model.User;
import com.pet.jiraliketracker.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO register(RegisterRequestDTO request) {
        String hashedPassword = passwordEncoder.encode(request.password);
        userRepository.save(new User(request.username, request.email, hashedPassword, "CLIENT"));
        return new UserResponseDTO(request.email, request.username);
    }

    public UserResponseDTO login(LoginRequestDTO loginDto) {

        // Тут спрінг знайде CustomUserDetailsService, викличе loadUserByUsername і у ньому звірить паролі
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        // Якщо паролі не співпадуть, Spring викине тут виключення
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        UserResponseDTO responseDto = new UserResponseDTO(user.getEmail(), user.getUsername());
        // responseDto.setToken("здесь_будет_сгенерированный_jwt_токен");
        return responseDto;
    }
}
