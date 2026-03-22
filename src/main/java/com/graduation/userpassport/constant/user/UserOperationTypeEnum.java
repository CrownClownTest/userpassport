package com.graduation.userpassport.constant.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum UserOperationTypeEnum {
    UPDATE_ME("UPDATE_ME", "用户修改自己信息");

    private final String id;
    private final String desc;

    private static final Map<String, UserOperationTypeEnum> ID_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(UserOperationTypeEnum::getId, Function.identity()));

    public static UserOperationTypeEnum fromId(String id) {
        UserOperationTypeEnum operationTypeEnum = ID_MAP.get(id);
        if (operationTypeEnum == null) {
            throw new IllegalArgumentException("不支持的操作类型id: " + id);
        }
        return operationTypeEnum;
    }
}
