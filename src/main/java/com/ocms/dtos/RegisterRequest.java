package com.ocms.dtos;
import lombok.Data;
@Data
public class RegisterRequest {
    private String username; // email
    private String password;
    private String role; // ADMIN, INSTRUCTOR, STUDENT
}
