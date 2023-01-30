package nextstep.auth.support;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.MemberRoleType;
import nextstep.auth.domain.MemberRoles;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.exception.AuthorizationException;
import nextstep.auth.exception.InvalidTokenException;
import nextstep.auth.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
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

    private boolean hasRequiredRoles(HttpServletRequest request, Object handler) {
        MemberRoleType[] requiredRoles = getRequiredRoles(handler);
        MemberRoles memberRoles = getMemberRoles(request);

        return memberRoles.hasRoles(requiredRoles);
    }

    private MemberRoles getMemberRoles(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        try{
            String subject = jwtTokenProvider.getSubject(token);
            return authService.findRoleByMemberName(subject);
        }catch (InvalidTokenException e){
            throw new AuthenticationException();
        }
    }

    private static MemberRoleType[] getRequiredRoles(Object handler) {
        HandlerMethod httpMethod = (HandlerMethod) handler;
        return httpMethod.getMethod()
                .getAnnotation(AuthorizationRequired.class)
                .value();
    }

    private static boolean isAuthorizationRequiredMethod(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AuthorizationRequired authorizationRequired = handlerMethod.getMethod()
                .getAnnotation(AuthorizationRequired.class);

        return !Objects.isNull(authorizationRequired);
    }
}
