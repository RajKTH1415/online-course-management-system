package com.ocms.service.Impl;

import com.ocms.dtos.RegisterRequest;
import com.ocms.entity.User;
import com.ocms.enums.Role;
import com.ocms.exception.CustomException;
import com.ocms.repository.UserRepository;
import com.ocms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public User register(RegisterRequest req) {
        if (userRepository.findByEmail(req.getUsername()).isPresent())
            throw new CustomException("Email already exists");
        Role role;
        try {
            role = Role.valueOf(req.getRole().toUpperCase());
        } catch (Exception e) {
            role = Role.STUDENT;
        }
        User u = User.builder()
                .email(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .enabled(true)
                .blocked(false)
                .build();
        return userRepository.save(u);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException("User not found"));
    }

    @Override
    public void blockUser(Long id) {

        User u = userRepository.findById(id).orElseThrow(() -> new CustomException("User not found"));
        u.setBlocked(true);
        userRepository.save(u);
    }

    @Override
    public void unblockUser(Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new CustomException("User not found"));
        u.setBlocked(false);
        userRepository.save(u);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new CustomException("User not found: " + username));
    }


}
