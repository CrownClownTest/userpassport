package com.graduation.userpassport.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户查询请求")
public class UserQueryRequest {
    @NotNull
    @Schema(description = "查询类型ID，1-用户ID，2-手机号，3-邮箱", example = "1")
    private Integer queryTypeId;

    @NotBlank
    @Schema(description = "查询值", example = "1")
    private String queryValue;
}
