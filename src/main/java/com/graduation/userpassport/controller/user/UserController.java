package com.graduation.userpassport.controller.user;

import com.graduation.userpassport.dto.user.LoginRequest;
import com.graduation.userpassport.dto.user.LoginResponse;
import com.graduation.userpassport.dto.user.RegisterUserRequest;
import com.graduation.userpassport.dto.user.UserInfoDTO;
import com.graduation.userpassport.dto.user.UserMeRequest;
import com.graduation.userpassport.dto.user.UserMeResponse;
import com.graduation.userpassport.dto.user.UserQueryRequest;
import com.graduation.userpassport.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "用户接口")
@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfoDTO register(@Valid @RequestBody RegisterUserRequest request) {
        try {
            return userService.register(request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @Operation(summary = "查询用户")
    @PostMapping("/query")
    public UserInfoDTO queryUser(@Valid @RequestBody UserQueryRequest request) {
        try {
            return userService.query(request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        try {
            return userService.login(request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex);
        }
    }

    @Operation(
            summary = "使用JWT查询当前用户信息",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Bearer <JWT>"
                    )
            }
    )
    @PostMapping("/me")
    public UserMeResponse me(@Valid @RequestBody UserMeRequest request) {
        try {
            return userService.me(request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex);
        }
    }
}
