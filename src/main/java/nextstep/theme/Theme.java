package nextstep.theme;

import static nextstep.utils.Validator.checkFieldIsNull;

public class Theme {
    private Long id;
    private final String name;
    private final String desc;
    private final int price;

    private Theme(String name, String desc, int price) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        validateFields();
    }

    public static Theme giveId(Theme theme, Long id){
        checkFieldIsNull(id, "id");
        theme.id = id;
        return theme;
    }

    public static ThemeBuilder builder(){
        return new ThemeBuilder();
    }

    public static class ThemeBuilder {
        private String name;

        private String desc;
        private int price;
        public ThemeBuilder name(String name){
            this.name = name;
            return this;
        }

        public ThemeBuilder desc(String desc){
            this.desc = desc;
            return this;
        }
        public ThemeBuilder price(int price){
            this.price = price;
            return this;
        }
        public Theme build(){
            return new Theme(name, desc, price);
        }

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

    private void validateFields() {
        checkFieldIsNull(name, "name");
        checkFieldIsNull(desc, "desc");
        checkFieldIsNull(price, "price");
    }

}
