package roomwaiting.auth.interceptor;

import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static roomwaiting.support.Messages.EMPTY_TOKEN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = request.getHeader(AUTHORIZATION);
        if (accessToken == null) {
            throw new AuthorizationServiceException(EMPTY_TOKEN.getMessage());
        }
        return true;
    }
}