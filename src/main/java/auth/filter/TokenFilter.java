package auth.filter;

import auth.AuthenticationException;
import auth.JwtTokenProvider;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenFilter implements Filter {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String credential = httpServletRequest.getHeader("authorization").split(" ")[1];

            String id = jwtTokenProvider.getPrincipal(credential);
            String role = jwtTokenProvider.getRole(credential);

            request.setAttribute("id", id);
            request.setAttribute("role", role);
        } catch (Exception e) {
            throw new AuthenticationException();
        }
        chain.doFilter(request, response);
    }
}
