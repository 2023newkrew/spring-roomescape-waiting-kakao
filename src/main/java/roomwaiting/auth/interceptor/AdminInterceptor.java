package roomwaiting.auth.interceptor;

import roomwaiting.auth.token.JwtTokenProvider;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static roomwaiting.nextstep.member.Role.ADMIN;
import static roomwaiting.support.Messages.EMPTY_TOKEN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static roomwaiting.support.Messages.NOT_ALLOWED_SERVICE;

public class AdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public AdminInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = request.getHeader(AUTHORIZATION);
        if (accessToken == null) {
            throw new AuthorizationServiceException(EMPTY_TOKEN.getMessage());
        }
        String credential = accessToken.split(" ")[1];
        String role = jwtTokenProvider.getRole(credential);
        if (!Objects.equals(role, ADMIN.name())) {
            throw new AuthorizationServiceException(NOT_ALLOWED_SERVICE.getMessage());
        }
        return true;
    }
}
