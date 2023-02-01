package com.authorizationserver.domains.authorization;

import com.authorizationserver.infrastructures.jwt.TokenData;
import com.authorizationserver.domains.authorization.entities.UserDetailsEntity;
import com.authorizationserver.domains.authorization.enums.RoleType;
import com.authorizationserver.interfaces.dto.TokenRequest;
import com.authorizationserver.interfaces.dto.TokenResponse;
import com.authorizationserver.domains.authorization.exceptions.AuthenticationException;
import com.authorizationserver.domains.authorization.exceptions.AuthenticationErrorMessageType;
import com.authorizationserver.infrastructures.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthorizationService {

    private final JwtTokenProvider provider;

    private final AuthRepository authRepository;


    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetailsEntity userDetailsEntity = authRepository.getByUsername(tokenRequest.getUsername());
        if (isNullOrWrongPassword(userDetailsEntity, tokenRequest.getPassword())) {
            throw new AuthenticationException(AuthenticationErrorMessageType.INVALID_USERNAME_OR_PASSWORD);
        }
        RoleType role = userDetailsEntity.getRole();
        TokenData tokenData = new TokenData(userDetailsEntity.getId(), role.toString());

        return new TokenResponse(provider.createToken(tokenData));
    }

    private boolean isNullOrWrongPassword(UserDetailsEntity member, String password) {
        return Objects.isNull(member) || member.isWrongPassword(password);
    }
}
