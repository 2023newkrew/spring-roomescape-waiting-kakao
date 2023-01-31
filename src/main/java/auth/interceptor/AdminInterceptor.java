package auth.interceptor;

import auth.exception.AuthExceptionCode;
import auth.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import nextstep.member.Role;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String role = (String) request.getAttribute("role");
        if (!Role.ADMIN.name().equals(role)) {
            throw new AuthenticationException(AuthExceptionCode.NOT_ADMIN);
        }

        return true;
    }
}
