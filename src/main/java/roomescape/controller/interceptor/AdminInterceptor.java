package roomescape.controller.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.annotation.Admin;
import roomescape.controller.errors.ErrorCode;
import roomescape.service.JWTProvider;
import roomescape.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {
    private final JWTProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        var handlerMethod = (HandlerMethod) handler;
        var auth = handlerMethod.getMethodAnnotation(Admin.class);
        if (auth == null) {
            return true;
        }

        var token = jwtProvider.getTokenFromAuthHeader(request.getHeader("Authorization"));
        if (token.isEmpty()) {
            throw new ServiceException(ErrorCode.INVALID_BEARER);
        }
        if (jwtProvider.isInvalidJWT(token.get())) {
            throw new ServiceException(ErrorCode.INVALID_TOKEN);
        }
        if (!jwtProvider.getIsAdmin(token.get())) {
            throw new ServiceException(ErrorCode.REQUIRED_ADMIN);
        }
        return true;
    }
}
