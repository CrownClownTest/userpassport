package com.graduation.userpassport.converter.identity;

import com.graduation.userpassport.dto.identity.IdentityDefinitionDTO;
import com.graduation.userpassport.resource.user.entity.IdentityDefinitionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IdentityConverter {
    public List<IdentityDefinitionDTO> toDTOs(List<IdentityDefinitionEntity> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(e -> IdentityDefinitionDTO.builder()
                        .code(e.getCode())
                        .name(e.getName())
                        .build())
                .toList();
    }
}
