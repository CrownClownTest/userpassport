package com.graduation.userpassport.service.identity;

import com.graduation.userpassport.dto.identity.AdminIdentityListRequest;
import com.graduation.userpassport.dto.identity.IdentityListRequest;
import com.graduation.userpassport.dto.identity.IdentityListResponse;

public interface IdentityService {
    IdentityListResponse adminList(AdminIdentityListRequest request);

    IdentityListResponse list(IdentityListRequest request);
}
