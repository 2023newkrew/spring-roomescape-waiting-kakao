package auth.support;

import auth.domain.RoleType;
import auth.domain.RoleTypes;
import auth.exception.AuthenticationException;
import auth.exception.AuthorizationException;
import auth.exception.InvalidTokenException;
import auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RequiredArgsConstructor
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!isAuthorizationRequiredMethod(handler)){
            return true;
        }

        if(!hasRequiredRoles(request, handler)){
            throw new AuthorizationException();
        }

        return super.preHandle(request, response, handler);
    }

    private static boolean isAuthorizationRequiredMethod(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AuthorizationRequired authorizationRequired = handlerMethod.getMethod()
                .getAnnotation(AuthorizationRequired.class);

        return !Objects.isNull(authorizationRequired);
    }

    private boolean hasRequiredRoles(HttpServletRequest request, Object handler) {
        RoleType[] requiredRoles = getRequiredRoles(handler);
        RoleTypes roleTypes = getMemberRoles(request);

        return roleTypes.hasRoles(requiredRoles);
    }

    private static RoleType[] getRequiredRoles(Object handler) {
        HandlerMethod httpMethod = (HandlerMethod) handler;
        return httpMethod.getMethod()
                .getAnnotation(AuthorizationRequired.class)
                .value();
    }

    private RoleTypes getMemberRoles(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        try{
            String subject = jwtTokenProvider.getSubject(token);
            return authService.findRoleByMemberName(subject);
        }catch (InvalidTokenException e){
            throw new AuthenticationException();
        }
    }
}
