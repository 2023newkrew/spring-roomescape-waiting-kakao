package com.authorizationserver.domains.authorization;

import com.authorizationserver.domains.authorization.entities.UserDetailsEntity;

public interface AuthRepository {
    UserDetailsEntity getByUsername(String username);
}
