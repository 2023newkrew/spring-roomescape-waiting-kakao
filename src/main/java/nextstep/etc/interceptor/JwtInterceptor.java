package nextstep.etc.interceptor;

import auth.domain.UserDetails;
import auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final String accessTokenName;

    private final String loginUserName;

    private final JwtTokenProvider provider;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String bearerToken = request.getHeader(accessTokenName);
        if (Objects.isNull(bearerToken)) {
            return true;
        }
        UserDetails userDetails = provider.getUserDetails(bearerToken);
        request.setAttribute(loginUserName, userDetails);

        return true;
    }
}
