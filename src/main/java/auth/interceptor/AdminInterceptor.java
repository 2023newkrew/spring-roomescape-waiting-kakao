package auth.interceptor;

import auth.domain.TokenData;
import auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import nextstep.etc.exception.AuthenticationException;
import nextstep.etc.exception.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RequiredArgsConstructor
@Component
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
            throw new AuthenticationException(ErrorMessage.NOT_ADMIN);
        }
    }
}
