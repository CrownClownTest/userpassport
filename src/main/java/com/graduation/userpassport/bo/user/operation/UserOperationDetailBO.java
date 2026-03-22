package com.graduation.userpassport.bo.user.operation;

import com.graduation.userpassport.constant.user.UserInfoFieldEnum;
import com.graduation.userpassport.constant.user.UserOperationSourceEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOperationDetailBO {
    private UserOperationSourceEnum source;
    private String scene;
    private List<UserInfoFieldEnum> changedFields;
    private Boolean passwordChanged;
}
