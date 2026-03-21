package com.graduation.userpassport.bo.user;

import com.graduation.userpassport.constant.user.UserQueryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryBO {
    private UserQueryTypeEnum queryType;
    private String queryValue;
}
