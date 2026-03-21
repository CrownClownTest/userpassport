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
@Schema(description = "当前用户查询请求（基于 JWT）")
public class UserMeRequest {
    @Schema(description = "场景标识")
    private String scene;
}
