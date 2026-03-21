package com.graduation.userpassport.bo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoBO {
    private Long userId;
    private String email;
    private String phone;
    private String nickname;
    private LocalDateTime createdAt;
}
