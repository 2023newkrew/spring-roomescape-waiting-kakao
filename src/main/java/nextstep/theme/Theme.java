package nextstep.theme;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Theme {
    private Long id;
    private final String name;
    private final String desc;
    private final int price;

    @JsonCreator
    public Theme(Long id, String name, String desc, int price) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public Theme(String name, String desc, int price) {
        this.name = name;
        this.desc = desc;
        this.price = price;
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
