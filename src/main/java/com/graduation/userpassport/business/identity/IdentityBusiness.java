package com.graduation.userpassport.business.identity;

import com.graduation.userpassport.resource.user.entity.IdentityDefinitionEntity;
import com.graduation.userpassport.resource.user.entity.UserIdentityEntity;
import com.graduation.userpassport.resource.user.repository.IdentityDefinitionRepository;
import com.graduation.userpassport.resource.user.repository.UserIdentityRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IdentityBusiness {
    private static final String ACTIVE_STATUS = "ACTIVE";
    private static final String ADMIN_CODE = "admin";

    private final IdentityDefinitionRepository identityDefinitionRepository;
    private final UserIdentityRepository userIdentityRepository;

    public IdentityBusiness(IdentityDefinitionRepository identityDefinitionRepository,
                            UserIdentityRepository userIdentityRepository) {
        this.identityDefinitionRepository = identityDefinitionRepository;
        this.userIdentityRepository = userIdentityRepository;
    }

    public boolean isAdmin(Long userId) {
        if (userId == null) {
            return false;
        }
        List<UserIdentityEntity> identities =
                userIdentityRepository.findByUserIdAndStatusOrderByIdAsc(userId, ACTIVE_STATUS);
        return identities.stream().anyMatch(i -> ADMIN_CODE.equalsIgnoreCase(i.getIdentityValue()));
    }

    public List<IdentityDefinitionEntity> queryAllActive() {
        return identityDefinitionRepository.findByStatus(ACTIVE_STATUS);
    }

    public List<IdentityDefinitionEntity> queryByCodesActive(List<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return List.of();
        }
        return identityDefinitionRepository.findByCodeInAndStatus(codes, ACTIVE_STATUS);
    }
}
