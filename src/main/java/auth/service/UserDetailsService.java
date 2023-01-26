package auth.service;

import auth.dto.UserDetailsResponse;

public interface UserDetailsService {
    UserDetailsResponse getByUsername(String username);
}
