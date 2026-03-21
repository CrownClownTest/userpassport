package com.graduation.userpassport.business.user;

import com.graduation.common.jwt.JwtUtils;
import com.graduation.userpassport.bo.user.UserInfoBO;
import com.graduation.userpassport.bo.user.UserLoginBO;
import com.graduation.userpassport.bo.user.UserLoginResultBO;
import com.graduation.userpassport.resource.user.entity.LoginHistoryEntity;
import com.graduation.userpassport.resource.user.entity.UserInfoEntity;
import com.graduation.userpassport.resource.user.repository.LoginHistoryRepository;
import com.graduation.userpassport.resource.user.repository.UserInfoRepository;
import com.graduation.userpassport.utils.security.PasswordHashUtils;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Component
public class UserLoginBusiness {
    
    private final UserInfoRepository userInfoRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final JwtUtils jwtUtils;

    public UserLoginBusiness(UserInfoRepository userInfoRepository,
                             LoginHistoryRepository loginHistoryRepository,
                             JwtUtils jwtUtils) {
        this.userInfoRepository = userInfoRepository;
        this.loginHistoryRepository = loginHistoryRepository;
        this.jwtUtils = jwtUtils;
    }

    public UserLoginResultBO login(UserLoginBO loginBO) {
        String username = loginBO.getUsername();
        String password = loginBO.getPassword();

        // 尝试通过手机号、邮箱或用户ID查询用户
        Optional<UserInfoEntity> userOpt = findUserByUsername(username);
        
        if (userOpt.isEmpty()) {
            return UserLoginResultBO.failure("用户名或密码错误");
        }

        UserInfoEntity user = userOpt.get();
        
        // 验证密码
        if (!PasswordHashUtils.verify(password, user.getPassword())) {
            return UserLoginResultBO.failure("用户名或密码错误");
        }

        // 生成JWT token
        String token = jwtUtils.generateToken(user.getNickname(), user.getUserId());

        Claims claims = jwtUtils.parseClaims(token);
        LocalDateTime tokenExpire = LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
        loginHistoryRepository.save(LoginHistoryEntity.builder()
                .userId(user.getUserId())
                .token(token)
                .tokenId(claims.getId())
                .tokenExpire(tokenExpire)
                .loginIp(trimToLength(loginBO.getLoginIp(), 50))
                .loginDevice(trimToLength(loginBO.getLoginDevice(), 255))
                .status("SUCCESS")
                .tokenRevoked(Boolean.FALSE)
                .build());
        
        // 构建用户信息BO
        UserInfoBO userInfoBO = UserInfoBO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .build();

        return UserLoginResultBO.success(token, userInfoBO);
    }

    private String trimToLength(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.length() <= maxLength) {
            return trimmed;
        }
        return trimmed.substring(0, maxLength);
    }

    private Optional<UserInfoEntity> findUserByUsername(String username) {
        // 首先尝试作为手机号查询
        Optional<UserInfoEntity> userOpt = userInfoRepository.findByPhone(username);
        if (userOpt.isPresent()) {
            return userOpt;
        }

        // 然后尝试作为邮箱查询
        userOpt = userInfoRepository.findByEmail(username);
        if (userOpt.isPresent()) {
            return userOpt;
        }

        // 最后尝试作为用户ID查询（如果是数字）
        try {
            Long userId = Long.parseLong(username);
            return userInfoRepository.findById(userId);
        } catch (NumberFormatException e) {
            // 如果不是数字，忽略异常
        }

        return Optional.empty();
    }
}
