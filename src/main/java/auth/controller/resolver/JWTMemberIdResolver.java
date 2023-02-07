package auth.controller.resolver;


import auth.annotation.JWTMemberId;
import auth.exception.AuthException;
import auth.service.model.JWTTokenModel;
import errors.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class JWTMemberIdResolver implements HandlerMethodArgumentResolver {
    private final JWTTokenModel jwtTokenModel;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JWTMemberId.class)
               && parameter.getParameterType()
                           .isAssignableFrom(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        var token = jwtTokenModel.token();
        if (parameter.isOptional()) {
            return token.map(JWTTokenModel.Token::getMemberId);
        }
        if (token.isEmpty()) {
            throw new AuthException(ErrorCode.REQUIRE_TOKEN);
        }
        return token.get().getMemberId();
    }
}
