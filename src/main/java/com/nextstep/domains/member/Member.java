package com.nextstep.domains.member;

import com.authorizationserver.domains.authorization.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Member {

    @Getter
    @Setter
    private Long id;

    @Getter
    private final String username;

    @Getter
    private final String password;

    @Getter
    private final String name;

    @Getter
    private final String phone;

    @Getter
    private final RoleType role;
}
