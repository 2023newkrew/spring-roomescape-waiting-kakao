package roomescape.nextstep.theme;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class Theme {
    private Long id;
    private String name;
    private String desc;
    private int price;
}
