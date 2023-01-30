package nextstep.etc.resolver;

import auth.domain.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final String loginUserName;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        LoginUser loginUser = parameter.getParameterAnnotation(LoginUser.class);
        Class<?> parameterType = parameter.getParameterType();
        boolean isUserDetails = List.of(parameterType.getInterfaces()).contains(UserDetails.class);

        return Objects.nonNull(loginUser) && isUserDetails;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (Objects.isNull(request)) {
            return null;
        }

        return request.getAttribute(loginUserName);
    }
}