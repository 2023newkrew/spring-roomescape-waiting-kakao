package nextstep.member.exception;


import lombok.Getter;
import nextstep.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public enum MemberErrorMessage implements ErrorMessage {

    CONFLICT(HttpStatus.CONFLICT, "유저이름이 이미 존재합니다.");

    @Getter
    final HttpStatus httpStatus;

    @Getter
    final String errorMessage;

    MemberErrorMessage(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
