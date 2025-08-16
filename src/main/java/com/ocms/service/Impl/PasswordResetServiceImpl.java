package com.ocms.service.Impl;

import com.ocms.entity.User;
import com.ocms.exception.CustomException;
import com.ocms.repository.UserRepository;
import com.ocms.service.PasswordResetService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public PasswordResetServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }


    @Override
    public void generateResetToken(String username) {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new CustomException("User not found"));

        String token = UUID.randomUUID().toString(); // Unique reset token
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30)); // Token valid for 30 minutes
        userRepository.save(user);

        // Send token via email (real-world: use EmailService)
        System.out.println("Password reset link: http://localhost:8080/api/auth/reset-password?token=" + token);

    }

    @Override
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new CustomException("Invalid token"));


        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new CustomException("Token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

    }
}
