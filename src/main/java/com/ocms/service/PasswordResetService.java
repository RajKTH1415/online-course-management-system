package com.ocms.service;

public interface PasswordResetService {

       void generateResetToken(String username);
      void resetPassword(String token, String newPassword);
}
