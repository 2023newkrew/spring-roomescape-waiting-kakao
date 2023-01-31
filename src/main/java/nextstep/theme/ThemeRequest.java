package nextstep.theme;

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

    /* RequestBody에서 사용 */
    @SuppressWarnings("unused")
    public String getDesc() {
        return desc;
    }

    /* RequestBody에서 사용 */
    @SuppressWarnings("unused")
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
