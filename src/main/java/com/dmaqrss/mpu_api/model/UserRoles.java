package com.dmaqrss.mpu_api.model;

import lombok.Getter;

@Getter
public enum UserRoles {
    ADMIN("admin"),
    SELLER("seller"),
    USER("user");

    private final String role;

    UserRoles(String role) {
        this.role = role;
    }

}
