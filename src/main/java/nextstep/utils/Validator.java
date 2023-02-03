package nextstep.utils;

import static nextstep.utils.ValidateUtils.getType;

import java.util.Objects;
import nextstep.exception.BlankStringException;
import nextstep.exception.NullFieldException;
import org.springframework.util.StringUtils;

public class Validator {

    private Validator() {
    }

    public static <T> void checkFieldIsNull(T object, String fieldName) {
        if (object instanceof String && !StringUtils.hasText((String) object)) {
            throw new BlankStringException(fieldName, getType());
        }

        if (Objects.isNull(object)) {
            throw new NullFieldException(fieldName, getType());
        }
    }

}
