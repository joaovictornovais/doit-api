package com.joao.doit.domain.user;

import lombok.Getter;

@Getter
public enum UserRole {

    USER("user"),
    ADMIN("admin");

    private final String role;

    private UserRole(String role) {
        this.role = role;
    }

}
