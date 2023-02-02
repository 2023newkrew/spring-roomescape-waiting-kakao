package auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static auth.exception.AuthErrorCode.ACCESS_DENIED;

@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String credential = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        String role = jwtTokenProvider.getRole(credential);
        if (!Objects.equals(role, "ADMIN")) {
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.from(ACCESS_DENIED.getMessage())));
            response.sendError(ACCESS_DENIED.getHttpStatus().value());
        }
        return true;
    }
}
