package nextstep.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.domain.Theme;

@Getter
@RequiredArgsConstructor
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
