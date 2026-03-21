package com.graduation.userpassport.bo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResultBO {
    private boolean success;
    private String token;
    private UserInfoBO userInfo;
    private String errorMessage;

    public static UserLoginResultBO success(String token, UserInfoBO userInfo) {
        return UserLoginResultBO.builder()
                .success(true)
                .token(token)
                .userInfo(userInfo)
                .build();
    }

    public static UserLoginResultBO failure(String errorMessage) {
        return UserLoginResultBO.builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }
}