package com.graduation.userpassport.business.user;

import com.graduation.userpassport.bo.user.UserIdentityBO;
import com.graduation.userpassport.resource.user.entity.IdentityDefinitionEntity;
import com.graduation.userpassport.resource.user.entity.UserIdentityEntity;
import com.graduation.userpassport.resource.user.repository.IdentityDefinitionRepository;
import com.graduation.userpassport.resource.user.repository.UserIdentityRepository;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserIdentityBusiness {
    private static final String ACTIVE_STATUS = "ACTIVE";
    private final IdentityDefinitionRepository identityDefinitionRepository;
    private final UserIdentityRepository userIdentityRepository;

    public UserIdentityBusiness(IdentityDefinitionRepository identityDefinitionRepository,
                                UserIdentityRepository userIdentityRepository) {
        this.identityDefinitionRepository = identityDefinitionRepository;
        this.userIdentityRepository = userIdentityRepository;
    }

    public void createUserIdentities(Long userId, List<String> identityCodes) {
        Set<String> uniqueCodes = new LinkedHashSet<>(identityCodes);
        List<IdentityDefinitionEntity> definitions = identityDefinitionRepository
                .findByCodeInAndStatus(uniqueCodes, ACTIVE_STATUS);
        if (definitions.isEmpty()) {
            return;
        }
        List<UserIdentityEntity> entities = definitions.stream()
                .map(definition -> UserIdentityEntity.builder()
                        .userId(userId)
                        .identityType(definition.getName())
                        .identityValue(definition.getCode())
                        .status(ACTIVE_STATUS)
                        .build())
                .toList();
        userIdentityRepository.saveAll(entities);
    }

    public List<UserIdentityBO> queryActiveIdentities(Long userId) {
        if (userId == null) {
            return List.of();
        }
        List<UserIdentityEntity> identities = userIdentityRepository
                .findByUserIdAndStatusOrderByIdAsc(userId, ACTIVE_STATUS);
        return identities.stream()
                .map(identity -> UserIdentityBO.builder()
                        .identityCode(identity.getIdentityValue())
                        .identityName(identity.getIdentityType())
                        .build())
                .toList();
    }
}
