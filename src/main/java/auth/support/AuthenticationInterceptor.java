package auth.support;

import auth.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RequiredArgsConstructor
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!isLoginRequiredMethod(handler)){
            return true;
        }

        if(!hasValidToken(request)){
            throw new AuthenticationException();
        }

        return super.preHandle(request, response, handler);
    }

    private static boolean isLoginRequiredMethod(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired loginRequired = handlerMethod.getMethod()
                .getAnnotation(LoginRequired.class);

        return !Objects.isNull(loginRequired);
    }

    private boolean hasValidToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        return jwtTokenProvider.isValidToken(token);
    }
}
