package auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserDetail {
    private Long id;
    private String username;
    private String password;
    private String role;
}
