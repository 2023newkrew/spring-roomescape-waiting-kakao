package auth;

import nextstep.support.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private AuthDao authDao;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(AuthDao authDao, JwtTokenProvider jwtTokenProvider) {
        this.authDao = authDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = authDao.findUserDetailsByUserName(tokenRequest.getUsername())
                .orElseThrow(EntityNotFoundException::new);

        validatePassword(userDetails, tokenRequest);

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    private void validatePassword(UserDetails userDetails, TokenRequest tokenRequest) {
        if (!userDetails.getPassword().equals(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }
    }

    public UserDetails extractUserDetails(String credential) {
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
