package com.graduation.userpassport.constant.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum UserQueryTypeEnum {
    USER_ID(1, "用户ID"),
    PHONE(2, "手机号"),
    EMAIL(3, "邮箱");

    private final Integer id;
    private final String desc;

    private static final Map<Integer, UserQueryTypeEnum> ID_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(UserQueryTypeEnum::getId, Function.identity()));

    public static UserQueryTypeEnum fromId(Integer id) {
        UserQueryTypeEnum userQueryTypeEnum = ID_MAP.get(id);
        if (userQueryTypeEnum == null) {
            throw new IllegalArgumentException("不支持的查询类型id: " + id);
        }
        return userQueryTypeEnum;
    }
}
