package com.graduation.userpassport.service.user;

import com.graduation.userpassport.dto.user.LoginRequest;
import com.graduation.userpassport.dto.user.LoginResponse;
import com.graduation.userpassport.dto.user.RegisterUserRequest;
import com.graduation.userpassport.dto.user.UserInfoDTO;
import com.graduation.userpassport.dto.user.UserMeRequest;
import com.graduation.userpassport.dto.user.UserMeResponse;
import com.graduation.userpassport.dto.user.UserQueryRequest;
import com.graduation.userpassport.dto.user.UpdateUserMeRequest;

public interface UserService {
    UserInfoDTO register(RegisterUserRequest request);

    UserInfoDTO query(UserQueryRequest request);

    LoginResponse login(LoginRequest request);

    UserMeResponse me(UserMeRequest request);

    UserInfoDTO updateMe(UpdateUserMeRequest request);
}
