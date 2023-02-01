package com.authorizationserver.domains.authorization.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus;

    public AuthenticationException(AuthenticationErrorMessageType authenticationErrorMessageType) {
        super(authenticationErrorMessageType.getErrorMessage());

        this.httpStatus = authenticationErrorMessageType.httpStatus;
    }
}
