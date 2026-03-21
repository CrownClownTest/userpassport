package com.graduation.userpassport.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户身份信息")
public class UserIdentityDTO {
    @Schema(description = "身份编码", example = "admin")
    private String identityCode;

    @Schema(description = "身份名称", example = "管理员")
    private String identityName;
}

