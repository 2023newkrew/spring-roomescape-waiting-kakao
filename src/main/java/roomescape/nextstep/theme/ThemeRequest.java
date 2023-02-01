package roomescape.nextstep.theme;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class ThemeRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String desc;
    @PositiveOrZero
    private int price;

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
        return new Theme(
                this.name,
                this.desc,
                this.price
        );
    }
}
