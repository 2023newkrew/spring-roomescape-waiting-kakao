package nextstep.exceptions.exception;


public abstract class RestAPIException extends RuntimeException {
    public RestAPIException(String responseMessage) {
        super(responseMessage);
    }
}
