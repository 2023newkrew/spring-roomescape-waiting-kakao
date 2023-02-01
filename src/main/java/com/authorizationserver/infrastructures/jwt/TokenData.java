package com.authorizationserver.infrastructures.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TokenData {

    @Getter
    private final Long id;

    @Getter
    private final String role;
}
