package auth.resolver;

import auth.exception.AuthenticationException;
import auth.login.AbstractMember;
import auth.login.LoginService;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

public class LoginMemberArgumentResolver<T extends AbstractMember> implements HandlerMethodArgumentResolver {
    private final LoginService<T> loginService;

    public LoginMemberArgumentResolver(LoginService<T> loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        try {
            String credential = Objects.requireNonNull(webRequest.getHeader(HttpHeaders.AUTHORIZATION)).split(" ")[1];
            return loginService.extractMember(credential);
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }
}
