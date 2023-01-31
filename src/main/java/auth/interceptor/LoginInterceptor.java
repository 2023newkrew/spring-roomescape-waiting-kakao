package auth.interceptor;

import auth.UserAuthenticator;
import auth.exception.AuthExceptionCode;
import auth.exception.AuthenticationException;
import auth.jwt.JwtTokenProvider;
import auth.jwt.TokenExtractor;
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
                .orElseThrow(this::throwAuthenticationException);

        if (!jwtTokenProvider.validateToken(credential)) {
            throw throwAuthenticationException();
        }

        String id = jwtTokenProvider.getPrincipal(credential);
        String role = userAuthenticator.getRole(Long.parseLong(id))
                .orElseThrow(this::throwAuthenticationException);
        request.setAttribute("role", role);
        return true;
    }

    private AuthenticationException throwAuthenticationException() {
        return new AuthenticationException(AuthExceptionCode.AUTHENTICATION_FAIL);
    }
}
