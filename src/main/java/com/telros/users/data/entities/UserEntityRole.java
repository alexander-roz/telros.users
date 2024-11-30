package com.telros.users.data.entities;

import org.springframework.security.core.GrantedAuthority;

public enum UserEntityRole implements GrantedAuthority {
    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        System.out.println("> method getAuthority started and returned: " + name());
        return name();
    }
}
