package roomwaiting.nextstep.theme;

import org.h2.util.StringUtils;

import static roomwaiting.support.Messages.EMPTY_VALUE;

public class Theme {
    private Long id;
    private String name;
    private String description;
    private Long price;

    public Theme() {
    }

    public Theme(Long id, String name, String description, Long price) {
        checkEmptyValue(name, price);
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Theme(String name, String description, Long price) {
        checkEmptyValue(name, price);
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
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

    private void checkEmptyValue(String name, Long price){
        if (StringUtils.isNullOrEmpty(name)  || price == 0) {
            throw new NullPointerException(EMPTY_VALUE.getMessage());
        }
    }
}
