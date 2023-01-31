package roomescape.controller.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.annotation.Admin;
import roomescape.controller.errors.ErrorCode;
import roomescape.service.exception.ServiceException;
import roomescape.service.model.JWTTokenModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {
    private final JWTTokenModel jwtTokenModel;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Admin adminAnnotation = null;
        if (handler instanceof HandlerMethod handlerMethod) {
            adminAnnotation = handlerMethod.getMethodAnnotation(Admin.class);
        }
        if (Objects.isNull(adminAnnotation)) {
            return true;
        }
        var isAdminRequest = jwtTokenModel.token()
                                          .map(JWTTokenModel.Token::isAdmin)
                                          .orElse(false);
        if (!isAdminRequest) {
            throw new ServiceException(ErrorCode.REQUIRED_ADMIN);
        }
        return true;
    }
}
