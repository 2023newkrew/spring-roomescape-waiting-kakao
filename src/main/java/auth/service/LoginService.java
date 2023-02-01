package auth.service;

import auth.jwt.JwtTokenProvider;
import auth.UserDetails;
import auth.UserDetailsService;
import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import auth.exception.AuthenticationException;

public class LoginService {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails member = userDetailsService.findByUsername(tokenRequest.getUsername());
        if (member == null || member.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(member.getId() + "", member.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractUserDetails(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return userDetailsService.findById(id);
    }
}
