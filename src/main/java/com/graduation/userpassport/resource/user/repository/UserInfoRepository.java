package com.graduation.userpassport.resource.user.repository;

import com.graduation.userpassport.resource.user.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {
    boolean existsByPhoneOrEmail(String phone, String email);

    Optional<UserInfoEntity> findByPhone(String phone);

    Optional<UserInfoEntity> findByEmail(String email);
}
