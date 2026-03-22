package com.graduation.userpassport.bo.user.operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoSnapshotBO {
    private String email;
    private String phone;
    private String nickname;
}
