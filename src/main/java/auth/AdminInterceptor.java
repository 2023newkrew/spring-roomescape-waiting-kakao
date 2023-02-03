package auth;

import static nextstep.exception.ErrorMessage.NOT_AUTHORIZED;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nextstep.exception.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String credential = request.getHeader(HttpHeaders.AUTHORIZATION)
            .split(" ")[1];
        String role = jwtTokenProvider.getRole(credential);
        if (!Objects.equals(role, "ADMIN")) {
            throw new AuthenticationException(NOT_AUTHORIZED.getMessage());
        }

        return true;
    }
}
