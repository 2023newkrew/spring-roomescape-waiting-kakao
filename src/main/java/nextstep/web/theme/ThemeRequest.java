package nextstep.web.theme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
