package nextstep.theme;

public class Theme {
    private Long id;
    private String name;
    private String desc;
    private int price;

    /* RestAssured에서 사용 */
    @SuppressWarnings("unused")
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

    public static ThemeBuilder builder() {
        return new ThemeBuilder();
    }

    public static class ThemeBuilder {
        private Long id;
        private String name;
        private String desc;
        private int price;
        private ThemeBuilder() {
        }

        public ThemeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ThemeBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ThemeBuilder desc(String desc) {
            this.desc = desc;
            return this;
        }

        public ThemeBuilder price(int price) {
            this.price = price;
            return this;
        }

        public Theme build() {
            return new Theme(id, name, desc, price);
        }
    }
}
