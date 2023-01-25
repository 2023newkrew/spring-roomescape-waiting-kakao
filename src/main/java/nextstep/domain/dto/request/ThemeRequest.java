<<<<<<<< HEAD:src/main/java/nextstep/domain/dto/request/ThemeRequest.java
package nextstep.domain.dto.request;
========
package nextstep.domain.dto;
>>>>>>>> 59193cb (refactor: 패키지 구조 변경):src/main/java/nextstep/domain/dto/ThemeRequest.java

import nextstep.domain.persist.Theme;

public class ThemeRequest {
    private String name;
    private String desc;
    private int price;

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

    public Theme toEntity() {
        return new Theme(
                this.name,
                this.desc,
                this.price
        );
    }
}
