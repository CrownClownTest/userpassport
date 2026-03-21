package com.graduation.userpassport.resource.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "login_history")
public class LoginHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "userid", nullable = false)
    private Long userId;

    @Column(name = "login_time", nullable = false)
    private LocalDateTime loginTime;

    @Column(name = "token", nullable = false, length = 2048)
    private String token;

    @Column(name = "token_id", nullable = false)
    private String tokenId;

    @Column(name = "token_expire", nullable = false)
    private LocalDateTime tokenExpire;

    @Column(name = "login_ip", length = 50)
    private String loginIp;

    @Column(name = "login_device", length = 255)
    private String loginDevice;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "error_msg")
    private String errorMsg;

    @Column(name = "token_revoked", nullable = false)
    private Boolean tokenRevoked;

    @PrePersist
    public void prePersist() {
        if (loginTime == null) {
            loginTime = LocalDateTime.now();
        }
        if (tokenRevoked == null) {
            tokenRevoked = Boolean.FALSE;
        }
    }
}
