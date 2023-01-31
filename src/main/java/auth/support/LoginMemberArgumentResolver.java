package auth.support;

import auth.annotation.LoginMember;
import auth.token.JwtTokenExtractor;
import nextstep.exception.UnauthenticatedException;
import auth.service.LoginService;
import nextstep.error.ErrorCode;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final LoginService loginService;
    private final JwtTokenExtractor jwtTokenExtractor;

    public LoginMemberArgumentResolver(LoginService loginService, JwtTokenExtractor jwtTokenExtractor) {
        this.loginService = loginService;
        this.jwtTokenExtractor = jwtTokenExtractor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        try {
            String token = jwtTokenExtractor.extract(webRequest);
            return loginService.extractMemberDetails(token);
        } catch (Exception e) {
            throw new UnauthenticatedException(ErrorCode.AUTHENTICATION_FAIL);
        }
    }
}
