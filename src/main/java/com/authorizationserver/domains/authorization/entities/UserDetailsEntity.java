package com.authorizationserver.domains.authorization.entities;

import com.authorizationserver.domains.authorization.enums.RoleType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class UserDetailsEntity {
    private final Long id;
    private final String username;
    private final String password;
    private final RoleType role;

    public boolean isWrongPassword(String password) {
        return !Objects.equals(this.password, password);
    }
}
