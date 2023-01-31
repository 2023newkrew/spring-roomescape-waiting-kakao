package auth.interceptor;

import auth.token.JwtTokenExtractor;
import auth.token.JwtTokenProvider;
import nextstep.error.ErrorCode;
import nextstep.exception.UnauthorizedException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class AdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenExtractor jwtTokenExtractor;

    public AdminInterceptor(JwtTokenProvider jwtTokenProvider, JwtTokenExtractor jwtTokenExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenExtractor = jwtTokenExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = jwtTokenExtractor.extract(request);
        String role = jwtTokenProvider.getRole(token);

        if (!Objects.equals(role, "ADMIN")) {
            throw new UnauthorizedException(ErrorCode.FORBIDDEN);
        }

        return true;
    }
}
