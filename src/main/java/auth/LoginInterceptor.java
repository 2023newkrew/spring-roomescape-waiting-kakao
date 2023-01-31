package auth;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        NeedAuth needAuth = ((HandlerMethod) handler).getMethodAnnotation(NeedAuth.class);

        if (needAuth == null) {
            return true;
        }

        String accessToken = AuthorizationExtractor.extract(request);
        boolean validationResult = jwtTokenProvider.validateToken(accessToken);
        if (!validationResult) {
            throw new AuthenticationException();
        }

        if (needAuth.role().equals(NeedAuth.ADMIN)) {
            String role = jwtTokenProvider.getRole(accessToken);
            if (!Objects.equals(role, NeedAuth.ADMIN)) {
                throw new ForbiddenException();
            }
        }
        return true;
    }
}
