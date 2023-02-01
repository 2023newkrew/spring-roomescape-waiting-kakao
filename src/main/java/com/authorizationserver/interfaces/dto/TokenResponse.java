package com.authorizationserver.interfaces.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class TokenResponse {

    private final String accessToken;

    public TokenResponse() {
        this(null);
    }
}
