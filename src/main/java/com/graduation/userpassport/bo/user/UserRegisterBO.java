package com.graduation.userpassport.bo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterBO {
    private String email;
    private String phone;
    private String nickname;
    private String password;
    private List<String> identityCodes;
}
