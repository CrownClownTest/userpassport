package com.graduation.userpassport.bo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateMeBO {
    private Long userId;
    private String oldPassword;
    private String email;
    private String phone;
    private String nickname;
    private String newPassword;
    private String scene;
    private String ipAddress;
    private String userAgent;
}
