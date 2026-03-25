package com.graduation.userpassport.controller.identity;

import com.graduation.userpassport.dto.identity.AdminIdentityListRequest;
import com.graduation.userpassport.dto.identity.IdentityListRequest;
import com.graduation.userpassport.dto.identity.IdentityListResponse;
import com.graduation.userpassport.service.identity.IdentityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "身份接口")
@RestController
@RequestMapping("/api/identity")
public class IdentityController {
    private final IdentityService identityService;

    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Operation(
            summary = "管理员获取身份列表",
            description = "需要管理员权限：admin",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Bearer <JWT>"
                    )
            }
    )
    @PostMapping("/admin/list")
    public IdentityListResponse adminList(@Valid @RequestBody AdminIdentityListRequest request) {
        try {
            return identityService.adminList(request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage(), ex);
        }
    }

    @Operation(summary = "通用获取身份列表（注册场景默认返回user）")
    @PostMapping("/list")
    public IdentityListResponse list(@Valid @RequestBody IdentityListRequest request) {
        try {
            return identityService.list(request);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }
}
