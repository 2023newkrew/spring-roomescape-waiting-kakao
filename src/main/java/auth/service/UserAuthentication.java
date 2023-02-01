package auth.service;

import java.util.Optional;

public interface UserAuthentication {
    Optional<User> getUser(String username);

    record User(
            String subject,
            String username,
            String password,
            Boolean isAdmin
    ) {
    }
}
