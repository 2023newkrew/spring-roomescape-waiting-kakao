package auth.dto;

import auth.domain.UserRole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class UserDetailsResponse {
    private final Long id;
    private final String username;
    private final String password;
    private final UserRole role;
}
