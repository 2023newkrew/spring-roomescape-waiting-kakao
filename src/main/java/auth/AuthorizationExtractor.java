package auth;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Optional;

public class AuthorizationExtractor {
    public static final String AUTHORIZATION = "Authorization";
    public static String BEARER_TYPE = "Bearer";

    public static Optional<String > extract(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return Optional.of(authHeaderValue);
            }
        }
        return Optional.empty();
    }
}
