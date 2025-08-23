package com.ocms.service;

import com.ocms.dtos.RegisterRequest;
import com.ocms.entity.User;


public interface UserService  {
    User register(RegisterRequest req);
    User findByEmail(String email);
    User blockUser(Long id);
    User unblockUser(Long id);

    User findByUsername(String username);


}