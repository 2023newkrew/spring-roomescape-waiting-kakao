package app.nextstep.domain;

public class Theme {
    private Long id;
    private String name;
    private String desc;
    private Integer price;

    public Theme(Long id, String name, String desc, Integer price) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public Theme(String name, String desc, Integer price) {
        this(null, name, desc, price);
    }

    public Theme(Long id) {
        this(id, null, null, null);
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
