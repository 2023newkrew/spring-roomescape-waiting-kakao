package roomescape.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.annotation.Admin;
import roomescape.exception.AuthorizationException;
import roomescape.service.JWTProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {
    private final JWTProvider provider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var handlerMethod = (HandlerMethod) handler;
        var auth = handlerMethod.getMethodAnnotation(Admin.class);
        if (auth == null) {
            return true;
        }
        var token = request.getHeader("Authorization").split(" ")[1];
        if (!provider.getIsAdmin(token)) {
            throw new AuthorizationException();
        }
        return true;
    }
}
