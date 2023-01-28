package auth.service;

import auth.domain.UserDetails;
import auth.domain.UserDetailsRepository;
import auth.dto.request.LoginMember;
import auth.dto.request.TokenRequest;
import auth.dto.response.TokenResponse;
import auth.support.AuthenticationException;
import auth.support.JwtTokenProvider;

public class LoginService {
    private UserDetailsRepository userDetailsRepository;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(UserDetailsRepository userDetailsRepository, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsRepository = userDetailsRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userDetailsRepository.findByUsername(tokenRequest.getUsername());
        if (userDetails == null || userDetails.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public LoginMember extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return new LoginMember(id);
    }
}
