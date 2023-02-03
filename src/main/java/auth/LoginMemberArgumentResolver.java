package auth;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserDetailsService<?> userDetailsService;

    public LoginMemberArgumentResolver(UserDetailsService<?> userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        try {
            String credential = extractCredential(webRequest);
            return userDetailsService.extractMember(credential);
        } catch (Exception e) {
            return null;
        }
    }

    private static String extractCredential(NativeWebRequest webRequest) {
        String authorization = extractAuthorization(webRequest);
        return extractToken(authorization);
    }

    private static String extractAuthorization(NativeWebRequest webRequest) {
        String header = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            throw new AuthenticationException();
        }
        return header;
    }

    private static String extractToken(String authorization) {
        String[] values = authorization.split(" ");
        if (values.length != 2) {
            throw new AuthenticationException();
        }
        return values[1];
    }
}
