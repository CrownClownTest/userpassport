package com.graduation.userpassport.resource.user.repository;

import com.graduation.userpassport.resource.user.entity.UserOperationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOperationLogRepository extends JpaRepository<UserOperationLogEntity, Long> {
}
