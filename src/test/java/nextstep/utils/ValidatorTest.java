package nextstep.utils;

import nextstep.exception.BlankStringException;
import nextstep.exception.NullFieldException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ValidatorTest {

    static class MyClass {
        String tmp;

        public MyClass(String tmp) {
            Validator.checkFieldIsNull(tmp, "tmp");
            this.tmp = tmp;
        }

    }

    @Test
    void ifNullNullCheck() {
        Assertions.assertThatThrownBy(() -> new MyClass(null)).isInstanceOf(NullFieldException.class)
                .hasMessageContaining("MyClass");
    }

    @Test
    void ifBlankNullCheck() {
        Assertions.assertThatThrownBy(() -> new MyClass("")).isInstanceOf(BlankStringException.class)
                .hasMessageContaining("MyClass");
    }

    @Test
    void ifNotNullNullCheck() {
        Assertions.assertThatCode(() -> new MyClass("tmp")).doesNotThrowAnyException();
    }

}