package nextstep.theme;

public class ThemeRequest {
    private String name;
    private String desc;
    private int price;

    public ThemeRequest() {}

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

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Theme toEntity() {
        return new Theme(
                this.name,
                this.desc,
                this.price
        );
    }
}
