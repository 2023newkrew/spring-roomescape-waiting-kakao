package auth.service;

import auth.domain.TokenData;
import auth.domain.UserRole;
import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import auth.dto.UserDetailsResponse;
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

    private final UserDetailsService userDetailsService;


    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetailsResponse userDetails = userDetailsService.getByUsername(tokenRequest.getUsername());
        if (isNullOrWrongPassword(userDetails, tokenRequest.getPassword())) {
            throw new AuthenticationException(ErrorMessage.INVALID_USERNAME_OR_PASSWORD);
        }
        UserRole role = userDetails.getRole();
        TokenData tokenData = new TokenData(userDetails.getId(), role.toString());

        return new TokenResponse(provider.createToken(tokenData));
    }

    private boolean isNullOrWrongPassword(UserDetailsResponse member, String password) {
        return Objects.isNull(member) || !Objects.equals(member.getPassword(), password);
    }
}
