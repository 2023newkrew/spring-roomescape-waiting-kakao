package auth;

import auth.support.exception.NotAdminRoleException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    // TODO NotAdminRoleException을 던지는 방식으로 변경 필요
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String credential = request.getHeader(HttpHeaders.AUTHORIZATION)
                .split(" ")[1];
        String role = jwtTokenProvider.getRole(credential);
        if (!Objects.equals(role, "ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자 권한이 없습니다.");
//            throw new NotAdminRoleException();
        }
        return true;
    }
}
