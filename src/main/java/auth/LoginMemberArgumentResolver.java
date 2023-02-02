package auth;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private LoginService loginService;

    public LoginMemberArgumentResolver(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String accessToken = AuthorizationExtractor.extract(request).orElseThrow(AuthenticationException::new);
        if (!loginService.isTokenValid(accessToken)) {
            throw new AuthenticationException();
        }
        UserDetails userDetails = loginService.extractMember(accessToken);
        if (userDetails == null) {
            throw new AuthenticationException();
        }
        return userDetails;
    }
}
