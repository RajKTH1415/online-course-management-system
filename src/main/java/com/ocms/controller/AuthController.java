package com.ocms.controller;

import com.ocms.dtos.ApiResponse;
import com.ocms.dtos.AuthResponse;
import com.ocms.dtos.LoginRequest;
import com.ocms.dtos.RegisterRequest;
import com.ocms.entity.User;
import com.ocms.security.JwtUtil;
import com.ocms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
//        User u = userService.register(req);
//        return ResponseEntity.ok(u);
//    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@RequestBody RegisterRequest req) {
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

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
//        Authentication auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
//        );
//        String token = jwtUtil.generateToken(auth);
//        return ResponseEntity.ok(new AuthResponse(token));
//    }


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
}
