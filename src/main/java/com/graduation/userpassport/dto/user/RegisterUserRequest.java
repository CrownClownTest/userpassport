package com.graduation.userpassport.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户注册请求")
public class RegisterUserRequest {
    @NotBlank
    @Email
    @Schema(description = "邮箱", example = "user@example.com")
    private String email;

    @NotBlank
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @NotBlank
    @Schema(description = "昵称", example = "张三")
    private String nickname;

    @NotBlank
    @Schema(description = "明文密码", example = "P@ssw0rd")
    private String password;

    @NotEmpty
    @Schema(description = "身份编码列表", example = "[\"user\",\"admin\"]")
    private List<@NotBlank String> identityCodes;
}
