package nextstep.theme;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ThemeRequest {

    private String name;
    private String desc;
    private int price;

    public Theme toEntity() {
        return Theme.builder()
                .name(name)
                .desc(desc)
                .price(price)
                .build();
    }
}
