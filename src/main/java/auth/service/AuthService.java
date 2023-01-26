package auth.service;

import auth.domain.TokenData;
import auth.domain.UserDetails;
import auth.domain.UserRole;
import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import auth.exception.AuthenticationException;
import auth.exception.ErrorMessage;
import auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final JwtTokenProvider provider;

    private final UserDetailsPrincipal userDetailsPrincipal;


    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userDetailsPrincipal.getByUsername(tokenRequest.getUsername());
        if (isNullOrWrongPassword(userDetails, tokenRequest.getPassword())) {
            throw new AuthenticationException(ErrorMessage.INVALID_USERNAME_OR_PASSWORD);
        }
        UserRole role = userDetails.getRole();
        TokenData tokenData = new TokenData(userDetails.getId(), role.toString());

        return new TokenResponse(provider.createToken(tokenData));
    }

    private boolean isNullOrWrongPassword(UserDetails member, String password) {
        return Objects.isNull(member) || member.isWrongPassword(password);
    }
}
