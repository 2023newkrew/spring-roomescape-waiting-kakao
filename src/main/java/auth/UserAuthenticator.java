package auth;

import auth.entity.UserDetails;

import java.util.Optional;

public interface UserAuthenticator {
    Optional<UserDetails> authenticate(String username, String password);

    boolean isAdmin(Long id);
}
