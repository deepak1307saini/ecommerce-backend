package com.example.ecommerce.util;

import com.example.ecommerce.enums.RoleEnum;

import java.util.List;

public final class Constants {
    public static final String PHONE_NUMBER_REGEX = "^\\+?[1-9]\\d{9,11}$";

    public static final List<RoleEnum> ROLE_NAMES = List.of(
            RoleEnum.PLATFORM_ADMIN,
            RoleEnum.TENANT_ADMIN,
            RoleEnum.USER
    );
    private Constants(){}
}
