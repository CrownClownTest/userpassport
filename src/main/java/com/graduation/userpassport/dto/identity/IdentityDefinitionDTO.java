package com.graduation.userpassport.dto.identity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "身份定义")
public class IdentityDefinitionDTO {
    @Schema(description = "身份编码", example = "user")
    private String code;

    @Schema(description = "身份名称", example = "用户")
    private String name;
}
