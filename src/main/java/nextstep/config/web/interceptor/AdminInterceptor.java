package nextstep.config.web.interceptor;

import auth.domain.UserDetails;
import auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final String loginUserName;

    private final JwtTokenProvider provider;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        UserDetails userDetails = (UserDetails) request.getAttribute(loginUserName);
        provider.validateAdmin(userDetails);

        return true;
    }
}
