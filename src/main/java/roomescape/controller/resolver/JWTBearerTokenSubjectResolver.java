package roomescape.controller.resolver;


import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.annotation.JWTBearerTokenSubject;
import roomescape.controller.errors.ErrorCode;
import roomescape.service.JWTProvider;
import roomescape.service.exception.ServiceException;

@Component
@RequiredArgsConstructor
public class JWTBearerTokenSubjectResolver implements HandlerMethodArgumentResolver {
    private final JWTProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JWTBearerTokenSubject.class)
                && parameter.getParameterType()
                            .isAssignableFrom(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        var token = jwtProvider.getTokenFromAuthHeader(webRequest.getHeader("Authorization"));
        if (token.isEmpty()) {
            throw new ServiceException(ErrorCode.INVALID_BEARER);
        }
        if (jwtProvider.isInvalidJWT(token.get())) {
            throw new ServiceException(ErrorCode.INVALID_TOKEN);
        }
        if (jwtProvider.isExpired(token.get())) {
            throw new ServiceException(ErrorCode.EXPIRED_TOKEN);
        }
        return jwtProvider.getSubject(token.get());
    }
}
