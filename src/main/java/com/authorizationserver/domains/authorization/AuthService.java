package com.authorizationserver.domains.authorization;

import com.authorizationserver.domains.authorization.exceptions.AuthenticationException;
import com.authorizationserver.domains.authorization.entities.UserDetailsEntity;
import com.authorizationserver.infrastructures.jwt.JwtTokenProvider;
import com.authorizationserver.interfaces.dtos.TokenRequest;
import com.authorizationserver.interfaces.dtos.TokenResponse;
import com.nextstep.domains.global.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private AuthRepository authDao;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(AuthRepository authDao, JwtTokenProvider jwtTokenProvider) {
        this.authDao = authDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetailsEntity userDetails = authDao.findUserDetailsByUserName(tokenRequest.getUsername())
                .orElseThrow(EntityNotFoundException::new);

        validatePassword(userDetails, tokenRequest);

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    private void validatePassword(UserDetailsEntity userDetails, TokenRequest tokenRequest) {
        if (!userDetails.getPassword().equals(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }
    }

    public UserDetailsEntity extractUserDetails(String credential) {
        Long id;
        try {
            id = extractPrincipal(credential);
        } catch (Exception e) {
            throw new AuthenticationException();
        }

        return authDao.findUserDetailsById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }
}
