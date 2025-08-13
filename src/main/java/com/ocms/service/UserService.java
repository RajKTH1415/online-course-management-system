package com.ocms.service;

import com.ocms.dtos.RegisterRequest;
import com.ocms.entity.User;


public interface UserService  {
    User register(RegisterRequest req);
    User findByEmail(String email);
    void blockUser(Long id);
    void unblockUser(Long id);
}