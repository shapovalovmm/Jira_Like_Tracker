package com.pet.jiraliketracker.service;

import com.pet.jiraliketracker.dto.LoginRequestDTO;
import com.pet.jiraliketracker.dto.RegisterRequestDTO;
import com.pet.jiraliketracker.dto.UserResponseDTO;
import com.pet.jiraliketracker.exception.DuplicateEmailException;
import com.pet.jiraliketracker.mapper.UserMapper;
import com.pet.jiraliketracker.model.User;
import com.pet.jiraliketracker.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponseDTO register(RegisterRequestDTO request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateEmailException("Користувач з таким email вже існує");
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User savedUser = userRepository.save(new User(request.getUsername(), request.getEmail(), hashedPassword, "CLIENT"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(savedUser.getEmail())
                .password(savedUser.getPassword())
                .roles(savedUser.getRole())
                .build();

        String token = jwtService.generateToken(userDetails);

        log.debug("Authenticated user: {}", savedUser.getEmail());

        return UserMapper.toDto(savedUser, token);
    }

    public UserResponseDTO login(LoginRequestDTO loginDto) {
        // Тут спрінг знайде CustomUserDetailsService, викличе loadUserByUsername Й у ньому звірить паролі
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        // Якщо паролі не збігаються, Spring викине тут виключення

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        log.debug("Authenticated user: {}", user.getEmail());

        return UserMapper.toDto(user, token);
    }
}
