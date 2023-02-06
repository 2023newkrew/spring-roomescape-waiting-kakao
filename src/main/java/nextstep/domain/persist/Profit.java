package nextstep.domain.persist;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Profit {
    private Long id;
    private LocalDateTime localDateTime;
    private int amount;
}
