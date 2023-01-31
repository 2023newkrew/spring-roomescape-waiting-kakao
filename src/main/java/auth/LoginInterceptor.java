package auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthenticator userAuthenticator;
    private final TokenExtractor tokenExtractor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String credential = tokenExtractor.extractToken(authorizationHeader)
                .orElseThrow(AuthenticationException::new);

        if (!jwtTokenProvider.validateToken(credential)) {
            throw new AuthenticationException();
        }

        String id = jwtTokenProvider.getPrincipal(credential);
        request.setAttribute("role", userAuthenticator.getRole(Long.parseLong(id)));
        return true;
    }
}
