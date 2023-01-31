package auth.login;

import auth.JwtTokenProvider;
import auth.UserDetails;
import auth.token.TokenRequest;
import auth.token.TokenResponse;
import org.springframework.context.ApplicationEventPublisher;

public class LoginService {

    private final ApplicationEventPublisher eventPublisher;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(ApplicationEventPublisher eventPublisher, JwtTokenProvider jwtTokenProvider) {
        this.eventPublisher = eventPublisher;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        LoginRequestEvent loginRequestEvent = new LoginRequestEvent(tokenRequest.getUsername(), tokenRequest.getPassword());

        eventPublisher.publishEvent(loginRequestEvent);

        String accessToken = jwtTokenProvider.createToken(loginRequestEvent.getId() + "", loginRequestEvent.getRole());

        return new TokenResponse(accessToken);
    }

    public UserDetails extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return new UserDetails(id);
    }
}
