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
public enum UserInfoFieldEnum {
    EMAIL("email", "邮箱"),
    PHONE("phone", "手机号"),
    NICKNAME("nickname", "昵称"),
    PASSWORD("password", "密码");

    private final String id;
    private final String desc;

    private static final Map<String, UserInfoFieldEnum> ID_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(UserInfoFieldEnum::getId, Function.identity()));

    public static UserInfoFieldEnum fromId(String id) {
        UserInfoFieldEnum fieldEnum = ID_MAP.get(id);
        if (fieldEnum == null) {
            throw new IllegalArgumentException("不支持的字段id: " + id);
        }
        return fieldEnum;
    }

    @JsonValue
    public String jsonValue() {
        return id;
    }
}
