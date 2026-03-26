package com.graduation.userpassport.resource.user.repository;

import com.graduation.userpassport.resource.user.entity.IdentityDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface IdentityDefinitionRepository extends JpaRepository<IdentityDefinitionEntity, Long> {
    List<IdentityDefinitionEntity> findByCodeInAndStatus(Collection<String> codes, String status);
    List<IdentityDefinitionEntity> findByStatus(String status);
}
