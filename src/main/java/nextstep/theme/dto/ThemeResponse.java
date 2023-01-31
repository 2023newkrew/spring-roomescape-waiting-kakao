package nextstep.theme.dto;

import lombok.Value;

@Value
public class ThemeResponse {
    Long id;
    String name;
    String desc;
    Integer price;
}
