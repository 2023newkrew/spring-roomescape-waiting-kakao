package nextstep.presentation;

import auth.AuthenticationException;
import auth.AuthorizationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.error.ErrorCode;
import nextstep.error.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthExceptionFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (AuthenticationException e) {
            setResponse(ErrorCode.AUTHENTICATION, response);
        } catch (AuthorizationException e) {
            setResponse(ErrorCode.AUTHORIZATION, response);
        }

    }

    private void setResponse(ErrorCode errorCode, HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
