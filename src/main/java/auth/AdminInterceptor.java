package auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String credential = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        Role role = jwtTokenProvider.getRole(credential);
        if (!Role.ADMIN.equals(role)) {
            throw new AuthenticationException();
        }
        return true;
    }
}
