package nextstep.theme;

import java.util.Objects;

public class Theme {
    private Long id;
    private String name;
    private String desc;
    private int price;

    public Theme() {
    }

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

    @Override
    public String toString() {
        return "Theme{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return price == theme.price && Objects.equals(id, theme.id) && Objects.equals(name, theme.name) && Objects.equals(desc, theme.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc, price);
    }
}
