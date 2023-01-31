package nextstep.theme.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nextstep.theme.Theme;

public class ThemeRequest {
    private String name;
    private String desc;
    private int price;

    @JsonCreator
    public ThemeRequest(@JsonProperty(value = "name") String name,
                        @JsonProperty(value = "desc") String desc,
                        @JsonProperty(value = "price") int price) {
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
