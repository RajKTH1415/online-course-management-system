package com.ocms.service;

public interface PasswordResetService {

    String generateResetToken(String username);

    String resetPassword(String token, String newPassword);
}
