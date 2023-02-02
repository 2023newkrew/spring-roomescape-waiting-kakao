package auth;

import auth.userauth.UserAuth;
import auth.userauth.UserAuthService;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final UserAuthService userAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(UserAuthService userAuthService, JwtTokenProvider jwtTokenProvider) {
        this.userAuthService = userAuthService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {

        UserAuth userAuth = userAuthService.loadUserByUsername(tokenRequest.getUsername());
        if (userAuth.checkWrongPassword(tokenRequest.getPassword())) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(userAuth.getId() + "", userAuth.getRole());
        System.out.println("GetId() : " + userAuth.getId());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        System.out.println(credential);
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserAuth extractMember(String credential) {
        Long id = extractPrincipal(credential);
        return userAuthService.loadUserById(id);
    }
}
