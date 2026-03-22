package com.graduation.userpassport.business.user;

import com.alibaba.fastjson2.JSON;
import com.graduation.userpassport.bo.user.operation.UserOperationLogCreateBO;
import com.graduation.userpassport.resource.user.entity.UserOperationLogEntity;
import com.graduation.userpassport.resource.user.repository.UserOperationLogRepository;
import org.springframework.stereotype.Component;

@Component
public class UserOperationLogBusiness {
    private final UserOperationLogRepository userOperationLogRepository;

    public UserOperationLogBusiness(UserOperationLogRepository userOperationLogRepository) {
        this.userOperationLogRepository = userOperationLogRepository;
    }

    public void record(UserOperationLogCreateBO logBO) {
        if (logBO == null) {
            return;
        }
        if (logBO.getOperationType() == null) {
            throw new IllegalArgumentException("操作类型不能为空");
        }
        if (logBO.getTargetUserId() == null) {
            throw new IllegalArgumentException("被操作人ID不能为空");
        }
        userOperationLogRepository.save(UserOperationLogEntity.builder()
                .operatorId(logBO.getOperatorId())
                .targetUserId(logBO.getTargetUserId())
                .operationType(logBO.getOperationType().getId())
                .operationDetail(toJson(logBO.getOperationDetail()))
                .oldValue(toJson(logBO.getOldValue()))
                .newValue(toJson(logBO.getNewValue()))
                .ipAddress(trimToLength(logBO.getIpAddress(), 50))
                .userAgent(trimToLength(logBO.getUserAgent(), 255))
                .build());
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return JSON.toJSONString(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("操作日志 JSON 序列化失败", e);
        }
    }

    private String trimToLength(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        if (trimmed.length() <= maxLength) {
            return trimmed;
        }
        return trimmed.substring(0, maxLength);
    }
}