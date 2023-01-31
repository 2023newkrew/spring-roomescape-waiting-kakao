package nextstep.theme.dto;

import nextstep.theme.Theme;

public class ThemeRequest {
    private final String name;
    private final String desc;
    private final int price;

    public ThemeRequest(String name, String desc, int price) {
        this.name = name;
        this.desc = desc;
        this.price = price;
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

    public Theme toEntity() {
        return Theme.builder()
                .desc(desc)
                .name(name)
                .price(price)
                .build();
    }
}
