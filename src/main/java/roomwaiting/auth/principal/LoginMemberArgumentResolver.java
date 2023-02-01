package roomwaiting.auth.principal;

import roomwaiting.auth.mvc.LoginService;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

import static roomwaiting.support.Messages.*;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final LoginService loginService;

    public LoginMemberArgumentResolver(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credential = Optional.ofNullable(webRequest.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1]).orElseThrow(()
                            -> new NullPointerException(LOGIN_NEEDS.getMessage()));
        return loginService.extractMember(credential);
    }
}
