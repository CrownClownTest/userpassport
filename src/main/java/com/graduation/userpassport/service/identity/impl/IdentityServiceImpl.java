package com.graduation.userpassport.service.identity.impl;

import com.graduation.common.jwt.JwtUserContext;
import com.graduation.common.jwt.JwtUserContextHolder;
import com.graduation.userpassport.business.identity.IdentityBusiness;
import com.graduation.userpassport.converter.identity.IdentityConverter;
import com.graduation.userpassport.dto.identity.AdminIdentityListRequest;
import com.graduation.userpassport.dto.identity.IdentityListRequest;
import com.graduation.userpassport.dto.identity.IdentityListResponse;
import com.graduation.userpassport.service.identity.IdentityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdentityServiceImpl implements IdentityService {
    private final IdentityBusiness identityBusiness;
    private final IdentityConverter identityConverter;

    public IdentityServiceImpl(IdentityBusiness identityBusiness,
                               IdentityConverter identityConverter) {
        this.identityBusiness = identityBusiness;
        this.identityConverter = identityConverter;
    }

    @Override
    public IdentityListResponse adminList(AdminIdentityListRequest request) {
        JwtUserContext ctx = JwtUserContextHolder.get();
        if (ctx == null || ctx.getUserId() == null) {
            throw new IllegalArgumentException("未登录或 JWT 无效");
        }
        if (!identityBusiness.isAdmin(ctx.getUserId())) {
            throw new IllegalArgumentException("没有权限访问该资源");
        }
        var list = identityBusiness.queryAllActive();
        return IdentityListResponse.builder()
                .identities(identityConverter.toDTOs(list))
                .build();
    }

    @Override
    public IdentityListResponse list(IdentityListRequest request) {
        var list = identityBusiness.queryByCodesActive(List.of("user"));
        return IdentityListResponse.builder()
                .identities(identityConverter.toDTOs(list))
                .build();
    }
}
