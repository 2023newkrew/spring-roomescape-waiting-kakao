package auth.service;

import auth.domain.UserDetails;

public interface UserDetailsPrincipal {
    UserDetails getByUsername(String username);
}
