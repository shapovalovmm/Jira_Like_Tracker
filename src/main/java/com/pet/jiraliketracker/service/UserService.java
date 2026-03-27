package com.pet.jiraliketracker.service;

import com.pet.jiraliketracker.dto.LoginRequestDTO;
import com.pet.jiraliketracker.dto.RegisterRequestDTO;
import com.pet.jiraliketracker.dto.UserResponseDTO;
import com.pet.jiraliketracker.model.User;
import com.pet.jiraliketracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CustomerUserDetailsService userDetailsService;

    public UserService(@Qualifier("userRepository") UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       CustomerUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }




}
