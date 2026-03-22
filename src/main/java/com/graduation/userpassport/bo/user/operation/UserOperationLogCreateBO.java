package com.graduation.userpassport.bo.user.operation;

import com.graduation.userpassport.constant.user.UserOperationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOperationLogCreateBO {
    private Long operatorId;
    private Long targetUserId;
    private UserOperationTypeEnum operationType;
    private UserOperationDetailBO operationDetail;
    private UserInfoSnapshotBO oldValue;
    private UserInfoSnapshotBO newValue;
    private String ipAddress;
    private String userAgent;
}
