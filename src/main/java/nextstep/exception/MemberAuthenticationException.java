package nextstep.exception;

import org.springframework.http.HttpStatus;

public class MemberAuthenticationException extends BaseWebException {
    public MemberAuthenticationException(String expected, String actual, String context, String type) {
        super(expected, actual, context, HttpStatus.UNAUTHORIZED.value(), type);
    }
}
