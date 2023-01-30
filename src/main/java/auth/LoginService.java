package auth;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private UserDetailService userDetailService;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(UserDetailService userDetailService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {

        UserDetails userDetails = userDetailService.loadUserByUsername(tokenRequest.getUsername());
        if (userDetails.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractMember(String credential) {
        Long id = extractPrincipal(credential);
        return userDetailService.loadUserById(id);
    }
}
