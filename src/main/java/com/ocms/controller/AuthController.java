package com.ocms.controller;

import com.ocms.dtos.LoginRequest;
import com.ocms.dtos.RegisterRequest;
import com.ocms.entity.User;
import com.ocms.security.JwtUtil;
import com.ocms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User user = userService.register(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        org.springframework.security.authentication.UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword());
        authenticationManager.authenticate(authToken);
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(request.getEmail(), request.getPassword(), new java.util.ArrayList<>());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(java.util.Map.of("token", token));
    }

}
