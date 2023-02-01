package com.authorizationserver.infrastructures.web;

import com.authorizationserver.infrastructures.jwt.TokenData;
import com.authorizationserver.domains.authorization.exceptions.AuthenticationException;
import com.authorizationserver.domains.authorization.exceptions.AuthenticationErrorMessageType;
import com.authorizationserver.infrastructures.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider provider;

    @Value("${security.jwt.token.access-token-name}")
    private String accessTokenName;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String accessToken = getAccessToken(request);
        validateAdmin(accessToken);

        return true;
    }

    private String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(accessTokenName);

        return provider.getValidToken(bearerToken);
    }

    private void validateAdmin(String accessToken) {
        TokenData tokenData = provider.getTokenData(accessToken);
        if (!Objects.equals(tokenData.getRole(), "ADMIN")) {
            throw new AuthenticationException(AuthenticationErrorMessageType.NOT_ADMIN);
        }
    }
}
