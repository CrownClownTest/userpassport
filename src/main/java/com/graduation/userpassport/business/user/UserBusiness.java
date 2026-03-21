package com.graduation.userpassport.business.user;

import com.graduation.userpassport.bo.user.UserInfoBO;
import com.graduation.userpassport.bo.user.UserQueryBO;
import com.graduation.userpassport.bo.user.UserRegisterBO;
import com.graduation.userpassport.resource.user.entity.UserInfoEntity;
import com.graduation.userpassport.resource.user.repository.UserInfoRepository;
import com.graduation.userpassport.utils.security.PasswordHashUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserBusiness {
    private final UserInfoRepository userInfoRepository;

    public UserBusiness(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public UserInfoBO register(UserRegisterBO registerBO) {
        if (userInfoRepository.existsByPhoneOrEmail(registerBO.getPhone(), registerBO.getEmail())) {
            throw new IllegalArgumentException("手机号或邮箱已注册");
        }
        UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                .email(registerBO.getEmail())
                .phone(registerBO.getPhone())
                .nickname(registerBO.getNickname())
                .password(PasswordHashUtils.hash(registerBO.getPassword()))
                .build();
        UserInfoEntity savedUser = userInfoRepository.save(userInfoEntity);
        return toBO(savedUser);
    }

    public UserInfoBO query(UserQueryBO queryBO) {
        Optional<UserInfoEntity> userOpt = switch (queryBO.getQueryType()) {
            case USER_ID -> queryByUserId(queryBO.getQueryValue());
            case PHONE -> userInfoRepository.findByPhone(queryBO.getQueryValue());
            case EMAIL -> userInfoRepository.findByEmail(queryBO.getQueryValue());
        };
        UserInfoEntity userInfoEntity = userOpt.orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return toBO(userInfoEntity);
    }

    public UserInfoBO queryByUserId(Long userId) {
        UserInfoEntity userInfoEntity = userInfoRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        return toBO(userInfoEntity);
    }

    private Optional<UserInfoEntity> queryByUserId(String queryValue) {
        try {
            return userInfoRepository.findById(Long.parseLong(queryValue));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("USER_ID 查询值必须是数字");
        }
    }

    private UserInfoBO toBO(UserInfoEntity userInfoEntity) {
        return UserInfoBO.builder()
                .userId(userInfoEntity.getUserId())
                .email(userInfoEntity.getEmail())
                .phone(userInfoEntity.getPhone())
                .nickname(userInfoEntity.getNickname())
                .createdAt(userInfoEntity.getCreatedAt())
                .build();
    }
}
