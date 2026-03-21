package com.graduation.userpassport.bo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginBO {
    private String username;
    private String password;
    private String loginIp;
    private String loginDevice;
}
