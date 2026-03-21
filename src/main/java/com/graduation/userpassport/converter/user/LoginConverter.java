package com.graduation.userpassport.converter.user;

import com.graduation.userpassport.bo.user.UserLoginBO;
import com.graduation.userpassport.dto.user.LoginRequest;

public class LoginConverter {
    
    public static UserLoginBO toBO(LoginRequest request) {
        if (request == null) {
            return null;
        }
        return UserLoginBO.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
    }
}