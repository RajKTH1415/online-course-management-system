package com.ocms.dtos;
import lombok.Data;
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role;
}
