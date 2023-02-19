package app.nextstep.entity;

import app.nextstep.domain.Theme;

public class ThemeEntity {
    private Long id;
    private String name;
    private String desc;
    private Integer price;

    public ThemeEntity(Long id, String name, String desc, Integer price) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public Theme toTheme() {
        return new Theme(id, name, desc, price);
    }
}
