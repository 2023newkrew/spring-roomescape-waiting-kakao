package nextstep.exception;

import nextstep.theme.Theme;

public class NotExistEntityException extends RuntimeException {
    public NotExistEntityException(Class<?> Class) {
        super("존재하지 않는 " + Class.getName() + " 입니다.");
    }
}
