package auth;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {
    private JwtTokenProvider jwtTokenProvider;

    public AdminInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String credential = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        String role = jwtTokenProvider.getRole(credential);
        if (!Objects.equals(role, "ADMIN")) {
            throw new AuthorizationException("관리자 권한이 필요합니다.");
        }

        return true;
    }
}
