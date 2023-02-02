package auth.interceptor;

import auth.exception.AuthExceptionCode;
import auth.exception.AuthenticationException;
import auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    private final LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!loginService.isAuthorizedHeader(authorizationHeader)) {
            throw new AuthenticationException(AuthExceptionCode.AUTHENTICATION_FAIL);
        }
        return true;

    }
}
