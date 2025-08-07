package com.ocms.service;

import com.ocms.dtos.RegisterRequest;
import com.ocms.entity.User;
import com.ocms.exception.CustomException;
import com.ocms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new CustomException("Email already exists");
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(true)
                .build();
        return userRepository.save(user);
    }

    public void blockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }

    public void unblockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }
}