package nextstep.exception;

public class ReservationAuthorizationWebException extends AuthorizationWebException {
    public ReservationAuthorizationWebException(String expected, String actual, String context, String type) {
        super(expected, actual, context, type);
    }
}
