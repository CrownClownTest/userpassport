package com.graduation.userpassport.service.user.impl;

import com.graduation.common.jwt.JwtUserContext;
import com.graduation.common.jwt.JwtUserContextHolder;
import com.graduation.userpassport.business.user.UserBusiness;
import com.graduation.userpassport.business.user.UserIdentityBusiness;
import com.graduation.userpassport.business.user.UserLoginBusiness;
import com.graduation.userpassport.bo.user.UserLoginBO;
import com.graduation.userpassport.bo.user.UserLoginResultBO;
import com.graduation.userpassport.bo.user.UserRegisterBO;
import com.graduation.userpassport.converter.user.LoginConverter;
import com.graduation.userpassport.converter.user.UserConverter;
import com.graduation.userpassport.dto.user.LoginRequest;
import com.graduation.userpassport.dto.user.LoginResponse;
import com.graduation.userpassport.dto.user.RegisterUserRequest;
import com.graduation.userpassport.dto.user.UserInfoDTO;
import com.graduation.userpassport.dto.user.UserMeRequest;
import com.graduation.userpassport.dto.user.UserMeResponse;
import com.graduation.userpassport.dto.user.UserQueryRequest;
import com.graduation.userpassport.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserServiceImpl implements UserService {
    private final UserBusiness userBusiness;
    private final UserIdentityBusiness userIdentityBusiness;
    private final UserLoginBusiness userLoginBusiness;
    private final UserConverter userConverter;

    public UserServiceImpl(UserBusiness userBusiness,
                           UserIdentityBusiness userIdentityBusiness,
                           UserLoginBusiness userLoginBusiness,
                           UserConverter userConverter) {
        this.userBusiness = userBusiness;
        this.userIdentityBusiness = userIdentityBusiness;
        this.userLoginBusiness = userLoginBusiness;
        this.userConverter = userConverter;
    }

    @Override
    @Transactional
    public UserInfoDTO register(RegisterUserRequest request) {
        UserRegisterBO userRegisterBO = userConverter.toRegisterBO(request);
        UserInfoDTO userInfoDTO = userConverter.toDTO(userBusiness.register(userRegisterBO));
        userIdentityBusiness.createUserIdentities(userInfoDTO.getUserId(), userRegisterBO.getIdentityCodes());
        return userInfoDTO;
    }

    @Override
    public UserInfoDTO query(UserQueryRequest request) {
        return userConverter.toDTO(userBusiness.query(userConverter.toQueryBO(request)));
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        UserLoginBO loginBO = LoginConverter.toBO(request);
        HttpServletRequest httpServletRequest = currentRequest();
        if (httpServletRequest != null) {
            loginBO.setLoginIp(getClientIp(httpServletRequest));
            loginBO.setLoginDevice(getUserAgent(httpServletRequest));
        }
        UserLoginResultBO resultBO = userLoginBusiness.login(loginBO);
        
        if (!resultBO.isSuccess()) {
            throw new IllegalArgumentException(resultBO.getErrorMessage());
        }

        Long userId = resultBO.getUserInfo() == null ? null : resultBO.getUserInfo().getUserId();

        return LoginResponse.builder()
                .accessToken(resultBO.getToken())
                .tokenType("Bearer")
                .expiresIn(86400L) // 24小时
                .userInfo(userConverter.toDTO(resultBO.getUserInfo()))
                .identities(userConverter.toIdentityDTOs(userIdentityBusiness.queryActiveIdentities(userId)))
                .build();
    }

    private HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }
        return null;
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            int commaIndex = xForwardedFor.indexOf(',');
            String ip = commaIndex > 0 ? xForwardedFor.substring(0, commaIndex) : xForwardedFor;
            return ip.trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }

    private String getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        if (ua == null) {
            return null;
        }
        String trimmed = ua.trim();
        if (trimmed.length() <= 255) {
            return trimmed;
        }
        return trimmed.substring(0, 255);
    }

    @Override
    public UserMeResponse me(UserMeRequest request) {
        JwtUserContext ctx = JwtUserContextHolder.get();
        if (ctx == null || ctx.getUserId() == null) {
            throw new IllegalArgumentException("未登录或 JWT 无效");
        }

        Long userId = ctx.getUserId();
        return UserMeResponse.builder()
                .userInfo(userConverter.toDTO(userBusiness.queryByUserId(userId)))
                .identities(userConverter.toIdentityDTOs(userIdentityBusiness.queryActiveIdentities(userId)))
                .build();
    }
}
