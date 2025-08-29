package com.ocms.service.Impl;

import com.ocms.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {

    @Autowired
    private JwtUtil jwtUtil;

    private final Set<String> blacklist = new HashSet<>();

    public void logout(String token) {
        if (!jwtUtil.validate(token)) {
            throw new IllegalArgumentException("Invalid token");
        }
        blacklist.add(token);
    }
    public boolean isTokenBlacklisted(String token){
        return blacklist.contains(token);
    }
}
