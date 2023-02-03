package auth.controller.interceptor;

import auth.annotation.JWTExist;
import auth.exception.AuthException;
import auth.service.model.JWTTokenModel;
import errors.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JWTExistInterceptor implements HandlerInterceptor {
    private final JWTTokenModel jwtTokenModel;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        JWTExist jwtExistAnnotation = null;
        if (handler instanceof HandlerMethod handlerMethod) {
            jwtExistAnnotation = handlerMethod.getMethodAnnotation(JWTExist.class);
        }
        if (Objects.isNull(jwtExistAnnotation)) {
            return true;
        }
        var jwtExist = jwtTokenModel.token()
                                    .isPresent();
        if (!jwtExist) {
            throw new AuthException(ErrorCode.REQUIRED_TOKEN);
        }
        return true;
    }
}
