<<<<<<<< HEAD:src/main/java/nextstep/domain/dto/request/ThemeRequest.java
package nextstep.domain.dto.request;
========
package nextstep.domain.dto;
>>>>>>>> 59193cb (refactor: 패키지 구조 변경):src/main/java/nextstep/domain/dto/ThemeRequest.java

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.domain.persist.Theme;

@Getter
@AllArgsConstructor
public class ThemeRequest {
    private String name;
    private String desc;
    private int price;

    public Theme toEntity() {
        return new Theme(
                this.name,
                this.desc,
                this.price
        );
    }
}
