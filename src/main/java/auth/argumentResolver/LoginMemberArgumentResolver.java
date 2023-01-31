package auth.argumentResolver;

import auth.jwt.TokenExtractor;
import auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import nextstep.exception.RoomEscapeException;
import nextstep.exception.RoomEscapeExceptionCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final LoginService loginService;
    private final TokenExtractor tokenExtractor;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String credential = tokenExtractor.extractToken(webRequest.getHeader(HttpHeaders.AUTHORIZATION))
                .orElseThrow(() -> new RoomEscapeException(RoomEscapeExceptionCode.UNEXPECTED_EXCEPTION));
        return loginService.extractPrincipal(credential);
    }
}
