package roomwaiting.nextstep.theme;

public class ThemeRequest {
    private final String name;
    private final String description;
    private final int price;

    public ThemeRequest(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public Theme toEntity() {
        return new Theme(
                this.name,
                this.description,
                this.price
        );
    }
}
