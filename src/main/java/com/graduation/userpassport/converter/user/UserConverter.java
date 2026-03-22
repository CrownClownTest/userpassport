package com.graduation.userpassport.converter.user;

import com.graduation.userpassport.bo.user.UserIdentityBO;
import com.graduation.userpassport.bo.user.UserInfoBO;
import com.graduation.userpassport.bo.user.UserQueryBO;
import com.graduation.userpassport.bo.user.UserRegisterBO;
import com.graduation.userpassport.bo.user.UserUpdateMeBO;
import com.graduation.userpassport.constant.user.UserQueryTypeEnum;
import com.graduation.userpassport.dto.user.RegisterUserRequest;
import com.graduation.userpassport.dto.user.UserIdentityDTO;
import com.graduation.userpassport.dto.user.UserInfoDTO;
import com.graduation.userpassport.dto.user.UserQueryRequest;
import com.graduation.userpassport.dto.user.UpdateUserMeRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserConverter {
    public UserRegisterBO toRegisterBO(RegisterUserRequest request) {
        return UserRegisterBO.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .nickname(request.getNickname())
                .password(request.getPassword())
                .identityCodes(request.getIdentityCodes())
                .build();
    }

    public UserQueryBO toQueryBO(UserQueryRequest request) {
        return UserQueryBO.builder()
                .queryType(UserQueryTypeEnum.fromId(request.getQueryTypeId()))
                .queryValue(request.getQueryValue())
                .build();
    }

    public UserUpdateMeBO toUpdateMeBO(UpdateUserMeRequest request, Long userId) {
        return UserUpdateMeBO.builder()
                .userId(userId)
                .oldPassword(request.getOldPassword())
                .email(request.getEmail())
                .phone(request.getPhone())
                .nickname(request.getNickname())
                .newPassword(request.getNewPassword())
                .scene(request.getScene())
                .build();
    }

    public UserInfoDTO toDTO(UserInfoBO userInfoBO) {
        return UserInfoDTO.builder()
                .userId(userInfoBO.getUserId())
                .email(userInfoBO.getEmail())
                .phone(userInfoBO.getPhone())
                .nickname(userInfoBO.getNickname())
                .createdAt(userInfoBO.getCreatedAt())
                .build();
    }

    public List<UserIdentityDTO> toIdentityDTOs(List<UserIdentityBO> identityBOs) {
        if (identityBOs == null) {
            return List.of();
        }
        return identityBOs.stream()
                .map(identity -> UserIdentityDTO.builder()
                        .identityCode(identity.getIdentityCode())
                        .identityName(identity.getIdentityName())
                        .build())
                .toList();
    }
}
