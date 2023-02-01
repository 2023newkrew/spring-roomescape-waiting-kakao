package com.nextstep.infrastructures.web;

import com.authorizationserver.infrastructures.jwt.TokenData;
import com.authorizationserver.infrastructures.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UseContextArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider provider;

    @Value("${security.jwt.token.access-token-name}")
    private String accessTokenName;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        UseContext useContext = parameter.getParameterAnnotation(UseContext.class);
        boolean isLong = Long.class.equals(parameter.getParameterType());

        return Objects.nonNull(useContext) && isLong;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        String bearerToken = webRequest.getHeader(accessTokenName);
        String accessToken = provider.getValidToken(bearerToken);
        TokenData tokenData = provider.getTokenData(accessToken);

        return tokenData.getId();
    }
}