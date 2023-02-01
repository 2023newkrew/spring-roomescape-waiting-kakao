package com.authorizationserver.interfaces.dto;

import lombok.*;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class TokenRequest {

    @Getter
    private final String username;

    @Getter
    private final String password;

    public TokenRequest() {
        this(null, null);
    }
}
