package auth.service;

import auth.controller.dto.TokenRequest;
import auth.controller.dto.TokenResponse;
import auth.domain.UserDetails;
import auth.repository.UserDetailsDao;
import auth.support.AuthenticationException;
import auth.support.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserDetailsDao userDetailsDao;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userDetailsDao.findByUsername(tokenRequest.getUsername());
        if (userDetails == null || !userDetails.isValidPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }
        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole().name());
        return new TokenResponse(accessToken);
    }

    public long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractUserDetails(String credential) {
        long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return userDetailsDao.findById(id);
    }
}
