package com.graduation.userpassport.resource.user.repository;

import com.graduation.userpassport.resource.user.entity.LoginHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistoryEntity, Long> {
}
