package nextstep.dto.response;

import nextstep.domain.theme.Theme;

public class ThemeResponse {

    private Long id;
    private String name;
    private String desc;
    private int price;

    public ThemeResponse() {
    }

    public ThemeResponse(Theme theme) {
        this.id = theme.getId();
        this.name = theme.getName();
        this.desc = theme.getDesc();
        this.price = theme.getPrice();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrice() {
        return price;
    }
}
