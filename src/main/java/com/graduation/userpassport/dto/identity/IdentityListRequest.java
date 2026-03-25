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
@Schema(description = "通用身份列表请求")
public class IdentityListRequest {
    @Schema(description = "预留字段：场景标识，如register")
    private String scene;
}
