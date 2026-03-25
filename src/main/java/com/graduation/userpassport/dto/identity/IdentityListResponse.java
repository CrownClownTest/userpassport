package com.graduation.userpassport.dto.identity;

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
@Schema(description = "身份列表响应")
public class IdentityListResponse {
    @Schema(description = "身份列表")
    private List<IdentityDefinitionDTO> identities;
}
