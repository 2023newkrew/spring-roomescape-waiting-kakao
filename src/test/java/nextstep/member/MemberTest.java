package nextstep.member;

import nextstep.exception.BlankStringException;
import nextstep.exception.NullFieldException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void nullBuild() {
        Assertions.assertThatThrownBy(() -> Member.builder().build()).isInstanceOf(NullFieldException.class)
                .hasMessageContaining("Member");

    }

    @Test
    void stringBlankBuild() {
        Assertions.assertThatThrownBy(() -> Member.builder()
                        .phone("")
                        .name("")
                        .username("")
                        .password("")
                        .role("")
                        .build()).isInstanceOf(BlankStringException.class)
                .hasMessageContaining("Member");
    }

}