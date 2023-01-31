package roomescape.controller.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.service.JWTProvider;
import roomescape.service.model.JWTTokenModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JWTBearerInterceptor implements HandlerInterceptor {
    private static final Pattern BEARER_PATTERN = Pattern.compile("[bB]earer *(.+)");
    private final JWTProvider jwtProvider;
    private final JWTTokenModel jwtTokenModel;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        var authorizationHeaderValue = request.getHeader("Authorization");
        if (Objects.isNull(authorizationHeaderValue)) {
            return true;
        }
        var matcher = BEARER_PATTERN.matcher(authorizationHeaderValue);
        if (!matcher.matches()) {
            return true;
        }
        jwtTokenModel.initialize(jwtProvider.parse(matcher.group(1)));
        return true;
    }
}

