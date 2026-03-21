package com.graduation.userpassport.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "当前用户查询响应（基于 JWT）")
public class UserMeResponse {
    @Schema(description = "用户信息")
    private UserInfoDTO userInfo;

    @Schema(description = "用户当前身份信息")
    private List<UserIdentityDTO> identities;
}
