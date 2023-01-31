package auth.support.interceptor;

import auth.domain.annotation.Secured;
import auth.domain.enumeration.Roles;
import auth.support.AuthorizationException;
import auth.support.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (getSecuredAnnotation(handler).isPresent()) {
            String credential = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
            validateAccessLevel(handler, credential);
        }
        return true;
    }

    private void validateAccessLevel(Object handler, String credential) {
        int currentAccessLevel = Roles.of(jwtTokenProvider.getRole(credential)).getAccessLevel();
        int targetAccessLevel = getSecuredAnnotation(handler).get().role().getAccessLevel();

        if (currentAccessLevel < targetAccessLevel) {
            throw new AuthorizationException();
        }
    }

    private Optional<Secured> getSecuredAnnotation(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        return Optional.ofNullable(handlerMethod.getMethodAnnotation(Secured.class));
    }
}
