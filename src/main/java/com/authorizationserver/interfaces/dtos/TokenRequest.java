package com.authorizationserver.interfaces.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenRequest {
    private String username;
    private String password;

    @JsonCreator
    public TokenRequest(
            @JsonProperty(value = "username") String username,
            @JsonProperty(value = "password") String password
    ) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
