package roomescape.resolver;


import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.annotation.JWTBearerTokenSubject;
import roomescape.exception.AuthorizationException;
import roomescape.service.JWTProvider;

@Component
public class JWTBearerTokenSubjectResolver implements HandlerMethodArgumentResolver {
    private final JWTProvider jwtProvider;

    public JWTBearerTokenSubjectResolver(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JWTBearerTokenSubject.class)
                && parameter.getParameterType()
                            .isAssignableFrom(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        try {
            String authorizationHeader = webRequest.getHeader("Authorization");
            return jwtProvider.getSubject(authorizationHeader.split(" ")[1]);
        } catch (RuntimeException e) {
            throw new AuthorizationException();
        }
    }
}
