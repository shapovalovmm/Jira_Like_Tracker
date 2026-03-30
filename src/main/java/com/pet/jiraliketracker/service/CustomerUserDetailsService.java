package com.pet.jiraliketracker.service;

import com.pet.jiraliketracker.model.User;
import com.pet.jiraliketracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// Звертається до репозиторію, щоб дістати юзера і перекласти його дані у формат, який сприймає Spring
// Реалізує лише одну функцію - шукає користувача за унікальним полем
@Service
public class CustomerUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())  // Характеризує унікальне поле за яким шукається користувач
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
