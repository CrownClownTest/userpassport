package com.graduation.userpassport.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户修改自己信息请求（基于 JWT，userId 以 JWT 为准）")
public class UpdateUserMeRequest {
    @NotBlank
    @Schema(description = "原密码（任何信息修改都需要校验）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oldPassword;

    @Email
    @Schema(description = "新邮箱（可选）", example = "new_user@example.com")
    private String email;

    @Schema(description = "新手机号（可选）", example = "13800138000")
    private String phone;

    @Schema(description = "新昵称（可选）", example = "李四")
    private String nickname;

    @Schema(description = "新密码（可选，不返回也不记录明文）", example = "N3wP@ssw0rd")
    private String newPassword;

    @Schema(description = "场景标识（可选）")
    private String scene;
}
