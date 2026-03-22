package com.graduation.userpassport.constant.user;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum UserOperationSourceEnum {
    SELF("SELF", "用户本人"),
    ADMIN("ADMIN", "管理员"),
    SYSTEM("SYSTEM", "系统任务");

    private final String id;
    private final String desc;

    private static final Map<String, UserOperationSourceEnum> ID_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(UserOperationSourceEnum::getId, Function.identity()));

    public static UserOperationSourceEnum fromId(String id) {
        UserOperationSourceEnum sourceEnum = ID_MAP.get(id);
        if (sourceEnum == null) {
            throw new IllegalArgumentException("不支持的来源id: " + id);
        }
        return sourceEnum;
    }

    @JsonValue
    public String jsonValue() {
        return id;
    }
}
