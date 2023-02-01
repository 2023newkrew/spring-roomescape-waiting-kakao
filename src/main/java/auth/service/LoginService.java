package auth.service;

import auth.domain.UserDetails;
import auth.domain.UserDetailsRepository;
import auth.dto.request.LoginMember;
import auth.dto.request.TokenRequest;
import auth.dto.response.TokenResponse;
import auth.support.AuthenticationException;
import auth.support.JwtTokenProvider;

public class LoginService {

    private final UserDetailsRepository userDetailsRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(UserDetailsRepository userDetailsRepository, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsRepository = userDetailsRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = findByUsername(tokenRequest.getUsername());
        if (userDetails.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        return new TokenResponse(jwtTokenProvider.createToken(String.valueOf(userDetails.getId()), userDetails.getRole()));
    }

    public LoginMember extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return new LoginMember(id);
    }

    private UserDetails findByUsername(String username) {
        return userDetailsRepository.findByUsername(username)
                .orElseThrow(AuthenticationException::new);
    }
}
