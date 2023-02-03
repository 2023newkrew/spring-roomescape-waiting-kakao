package roomwaiting.nextstep.theme;

public class ThemeRequest {
    private final String name;
    private final String description;
    private final Long price;

    public ThemeRequest(String name, String description, Long price) {
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

    public Long getPrice() {
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
