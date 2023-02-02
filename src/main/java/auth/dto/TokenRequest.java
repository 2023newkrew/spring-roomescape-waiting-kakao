package auth.dto;

import lombok.Getter;

@Getter
public class TokenRequest {
    private String username;
    private String password;

    public TokenRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
