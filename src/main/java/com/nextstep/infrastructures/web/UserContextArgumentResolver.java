package com.nextstep.infrastructures.web;

import com.authorizationserver.domains.authorization.AuthService;
import com.authorizationserver.domains.authorization.exceptions.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserContextArgumentResolver implements HandlerMethodArgumentResolver {
    private AuthService authService;

    public UserContextArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserContext.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String credential;
        try {
            credential = webRequest.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        } catch (Exception e) {
            throw new AuthenticationException();
        }
        return authService.extractUserDetails(credential);
    }
}
