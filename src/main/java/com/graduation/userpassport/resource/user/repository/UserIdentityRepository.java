package com.graduation.userpassport.resource.user.repository;

import com.graduation.userpassport.resource.user.entity.UserIdentityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserIdentityRepository extends JpaRepository<UserIdentityEntity, Long> {
    List<UserIdentityEntity> findByUserIdAndStatusOrderByIdAsc(Long userId, String status);
}
