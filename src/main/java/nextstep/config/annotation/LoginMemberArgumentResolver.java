package nextstep.config.annotation;

import auth.JwtTokenProvider;
import auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import nextstep.member.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static auth.exception.AuthErrorCode.INVALID_USER;

@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        try {
            String credential = webRequest.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
            return memberService.findById(Long.valueOf(jwtTokenProvider.getPrincipal(credential)));
        } catch (Exception e) {
            throw new AuthException(INVALID_USER);
        }
    }
}
