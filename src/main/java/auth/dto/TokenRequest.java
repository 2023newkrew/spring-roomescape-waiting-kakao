package auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TokenRequest {
    private final String username;
    private final String password;

    public TokenRequest(String username, String password) {
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
