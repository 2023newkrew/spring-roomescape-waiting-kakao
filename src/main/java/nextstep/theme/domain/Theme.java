package nextstep.theme.domain;

import lombok.Value;

@Value
public class Theme {
    Long id;
    String name;
    String desc;
    Integer price;

    public ThemeEntity toEntity() {
        return new ThemeEntity(id, name, desc, price);
    }
}
