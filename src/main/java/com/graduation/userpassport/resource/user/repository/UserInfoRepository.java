package com.graduation.userpassport.resource.user.repository;

import com.graduation.userpassport.resource.user.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {
    boolean existsByPhoneOrEmail(String phone, String email);

    boolean existsByEmailAndUserIdNot(String email, Long userId);

    boolean existsByPhoneAndUserIdNot(String phone, Long userId);

    Optional<UserInfoEntity> findByPhone(String phone);

    Optional<UserInfoEntity> findByEmail(String email);
}
