package auth.interceptor;

import nextstep.exception.UnauthorizedException;
import auth.token.JwtTokenProvider;
import nextstep.error.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class AdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public AdminInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String credential = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        String role = jwtTokenProvider.getRole(credential);

        if (!Objects.equals(role, "ADMIN")) {
            throw new UnauthorizedException(ErrorCode.FORBIDDEN);
        }

        return true;
    }
}
