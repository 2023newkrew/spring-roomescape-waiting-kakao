package nextstep.theme.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Theme {

    @Getter
    @Setter
    private Long id;

    @Getter
    private final String name;

    @Getter
    private final String desc;

    @Getter
    private final Integer price;
}
