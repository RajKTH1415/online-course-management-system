package com.ocms.controller;

import com.ocms.dtos.*;
import com.ocms.entity.User;
import com.ocms.exception.CustomException;
import com.ocms.security.JwtUtil;
import com.ocms.service.Impl.PasswordResetServiceImpl;
import com.ocms.service.Impl.TokenBlacklistService;
import com.ocms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordResetServiceImpl passwordResetService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest req) {

        if (req.getUsername() == null || req.getUsername().isBlank() ||
                req.getPassword() == null || req.getPassword().isBlank()) {

            ApiResponse<Object> response = new ApiResponse<>(
                    false,
                    "Email and password must not be null or empty",
                    null,
                    "INVALID_INPUT"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
            User existingUser = userService.findByUsername(req.getUsername());
            if (existingUser != null) {
                ApiResponse<Object> response = new ApiResponse<>(
                        false,
                        "User already exists with this email",
                        null,
                        "DUPLICATE_USER"
                );
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
        } catch (Exception ignored) {

        }
        User savedUser = userService.register(req);

        Map<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("id", savedUser.getId());
        responseData.put("username", savedUser.getEmail());
        responseData.put("role", savedUser.getRole());

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                true,
                "User registered successfully",
                responseData
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        String token = jwtUtil.generateToken(auth);
        User user = userService.findByUsername(req.getUsername());

        Map<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("id", user.getId());
        responseData.put("username", user.getEmail());
        responseData.put("role", user.getRole());
        responseData.put("token", token);

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                true,
                "Login successful",
                responseData
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(
                            false,
                            "Token is missing. Please login again.",
                            null,
                            "AUTH-001"
                    ));
        }
        String token = authHeader.substring(7);
        try {
            tokenBlacklistService.logout(token);
            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Logout successful.",
                            Collections.singletonMap("token", token),
                            null
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(
                            false,
                            "Token is invalid. Please login again.",
                            null,
                            "AUTH-002"
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                            false,
                            "An error occurred while logging out.",
                            null,
                            "AUTH-500"
                    ));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            //passwordResetService.generateResetToken(request.getUsername());// this is for mail
            String token = passwordResetService.generateResetToken(request.getUsername());
            return ResponseEntity.ok(new ApiResponse<>(true, "Rest link sent to your  mail", token));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null, "USER_NOT_FOUND"));

        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(new ApiResponse<>(true, "Password reset successful", null));


        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null, "INVALID_TOKEN"));
        }
    }
}

//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
//        User u = userService.register(req);
//        return ResponseEntity.ok(u);
//    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
//        Authentication auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
//        );
//        String token = jwtUtil.generateToken(auth);
//        return ResponseEntity.ok(new AuthResponse(token));
//    }


