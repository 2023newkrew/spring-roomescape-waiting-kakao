package com.authorizationserver.domains.authorization;

import com.authorizationserver.domains.authorization.entities.UserDetailsEntity;

import java.util.Optional;

public interface AuthRepository {
    Optional<UserDetailsEntity> findUserDetailsByUserName(String userName);
    Optional<UserDetailsEntity> findUserDetailsById(Long id);
}
