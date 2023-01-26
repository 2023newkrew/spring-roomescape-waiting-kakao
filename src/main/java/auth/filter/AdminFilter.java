package auth.filter;

import auth.AuthorizationException;

import javax.servlet.*;
import java.io.IOException;
import java.util.Objects;

public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        Object role = request.getAttribute("role");
        if (Objects.isNull(role) || !role.toString().equals("ADMIN")) {
            throw new AuthorizationException();
        }

        chain.doFilter(request, response);
    }
}
