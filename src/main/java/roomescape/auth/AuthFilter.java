package roomescape.auth;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    JwtTokenProvider jwtTokenProvider;

    enum AuthLevel {UNNECESSARY, LOGIN, ADMIN}

    public AuthFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        AuthLevel requiredLevel = getRequiredLevel(httpServletRequest);
        if (requiredLevel == AuthLevel.UNNECESSARY) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String credential = httpServletRequest.getHeader("Authorization").split(" ")[1];
            if (!jwtTokenProvider.validateToken(credential)) {
                throw new AuthenticationException();
            }
            if (requiredLevel == AuthLevel.ADMIN && !jwtTokenProvider.getRole(credential).equals("ADMIN")) {
                throw new AuthenticationException();
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private AuthLevel getRequiredLevel(HttpServletRequest httpServletRequest) {
        String uri = httpServletRequest.getRequestURI();
        if (uri.startsWith("/admin"))
            return AuthLevel.ADMIN;

        if (uri.equals("/reservations") && httpServletRequest.getMethod().equalsIgnoreCase("GET"))
            return AuthLevel.UNNECESSARY;

        if (uri.startsWith("/reservation") || uri.equals("/members/me"))
            return AuthLevel.LOGIN;

        return AuthLevel.UNNECESSARY;
    }
}
