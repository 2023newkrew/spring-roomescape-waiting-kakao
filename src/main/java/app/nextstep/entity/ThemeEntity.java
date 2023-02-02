package app.nextstep.entity;

import app.nextstep.domain.Theme;

public class ThemeEntity {
    private Long id;
    private String name;
    private String desc;
    private int price;

    public ThemeEntity() {
    }

    public ThemeEntity(Long id, String name, String desc, int price) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public Theme toTheme() {
        return new Theme(id, name, desc, price);
    }
}
