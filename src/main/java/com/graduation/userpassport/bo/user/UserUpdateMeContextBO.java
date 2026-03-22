package com.graduation.userpassport.bo.user;

import com.graduation.userpassport.bo.user.operation.UserInfoSnapshotBO;
import com.graduation.userpassport.constant.user.UserInfoFieldEnum;
import com.graduation.userpassport.resource.user.entity.UserInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateMeContextBO {
    private UserUpdateMeBO request;
    private UserInfoEntity userInfoEntity;

    private String newEmail;
    private String newPhone;
    private String newNickname;
    private String newPassword;
    private String oldPassword;

    private boolean emailChanged;
    private boolean phoneChanged;
    private boolean nicknameChanged;
    private boolean passwordChanged;

    private UserInfoSnapshotBO oldSnapshot;
    private UserInfoSnapshotBO newSnapshot;
    private List<UserInfoFieldEnum> changedFields;
}
