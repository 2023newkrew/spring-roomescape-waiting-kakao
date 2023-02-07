package nextstep.theme;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ThemeRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String desc;
    @Min(0)
    @NotNull
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
