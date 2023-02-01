package nextstep.support;

import auth.support.exception.NotAdminRoleException;
import nextstep.support.error.ErrorCode;
import nextstep.support.error.ErrorResponse;
import nextstep.support.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyReservedScheduleException.class)
    ResponseEntity<ErrorResponse> handleAlreadyReservedScheduleException() {
        return ErrorResponse.toResponseEntity(ErrorCode.ALREADY_RESERVED_SCHEDULE);
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ErrorResponse> handleAuthenticationException() {
        return ErrorResponse.toResponseEntity(ErrorCode.UNAUTHENTICATED);
    }

    @ExceptionHandler(DuplicateReservationException.class)
    ResponseEntity<ErrorResponse> handleDuplicateReservationException() {
        return ErrorResponse.toResponseEntity(ErrorCode.DUPLICATE_RESERVATION);
    }

    @ExceptionHandler(DuplicateReservationWaitingException.class)
    ResponseEntity<ErrorResponse> handleDuplicateReservationWaitingException() {
        return ErrorResponse.toResponseEntity(ErrorCode.DUPLICATE_RESERVATION_WAITING);
    }

    @ExceptionHandler(NonExistMemberException.class)
    ResponseEntity<ErrorResponse> handleNonExistMemberException() {
        return ErrorResponse.toResponseEntity(ErrorCode.NON_EXIST_MEMBER);
    }

    @ExceptionHandler(NonExistReservationException.class)
    ResponseEntity<ErrorResponse> handleNonExistReservationException() {
        return ErrorResponse.toResponseEntity(ErrorCode.NON_EXIST_RESERVATION);
    }

    @ExceptionHandler(NonExistReservationWaitingException.class)
    ResponseEntity<ErrorResponse> handleNonExistReservationWaitingException() {
        return ErrorResponse.toResponseEntity(ErrorCode.NON_EXIST_RESERVATION_WAITING);
    }

    @ExceptionHandler(NonExistScheduleException.class)
    ResponseEntity<ErrorResponse> handleNonExistScheduleException() {
        return ErrorResponse.toResponseEntity(ErrorCode.NON_EXIST_SCHEDULE);
    }

    @ExceptionHandler(NonExistThemeException.class)
    ResponseEntity<ErrorResponse> handleNonExistThemeException() {
        return ErrorResponse.toResponseEntity(ErrorCode.NON_EXIST_THEME);
    }

    @ExceptionHandler(NotOwnReservationWaitingException.class)
    ResponseEntity<ErrorResponse> handleNotOwnReservationWaitingException() {
        return ErrorResponse.toResponseEntity(ErrorCode.NOT_OWN_RESERVATION_WAITING);
    }

    @ExceptionHandler(NotAdminRoleException.class)
    ResponseEntity<ErrorResponse> handleNotAdminRoleException() {
        return ErrorResponse.toResponseEntity(ErrorCode.NOT_ADMIN_ROLE);
    }
}
