package auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(request.getMethod().equals(HttpMethod.GET.name())) {
            return true;
        }
        if (accessToken == null) {
            throw new AuthenticationException();
        }
        return true;
    }
}
