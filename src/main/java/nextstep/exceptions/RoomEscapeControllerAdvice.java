package nextstep.exceptions;


import nextstep.exceptions.exception.RestAPIException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RoomEscapeControllerAdvice {

    @ExceptionHandler(RestAPIException.class)
    public ResponseEntity<String> handleRestAPIException(RestAPIException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(e.getMessage());
    }

}

