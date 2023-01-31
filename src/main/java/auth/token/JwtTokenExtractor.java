package auth.token;

import nextstep.error.ErrorCode;
import nextstep.exception.InvalidAuthorizationTokenException;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

public class JwtTokenExtractor {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_TYPE = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenExtractor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String extract(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        return extract(authorization);
    }

    public String extract(NativeWebRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        return extract(authorization);
    }

    public String extract(String authorization) {
        validateAuthorizationHeader(authorization);
        String token = authorization.substring(BEARER_TYPE.length()).split(",")[0].trim();
        validateToken(token);
        return token;
    }

    private void validateAuthorizationHeader(String authorization) {
        if (authorization == null) {
            throw new InvalidAuthorizationTokenException(ErrorCode.INVALID_TOKEN);
        }

        if (!authorization.trim().toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            throw new InvalidAuthorizationTokenException(ErrorCode.INVALID_TOKEN);
        }
    }

    private void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidAuthorizationTokenException(ErrorCode.TOKEN_EXPIRED);
        }
    }
}
