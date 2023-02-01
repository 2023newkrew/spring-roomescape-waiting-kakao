package nextstep.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.persist.Theme;

@NoArgsConstructor
@Getter
public class ThemeResponse {
    @Schema(description = "테마 id")
    private Long id;
    @Schema(description = "테마 이름")
    private String name;
    @Schema(description = "테마 설명")
    private String desc;
    @Schema(description = "테마 가격")
    private int price;

    public ThemeResponse(Theme theme) {
        this.id = theme.getId();
        this.name = theme.getName();
        this.desc = theme.getDesc();
        this.price = theme.getPrice();
    }
}
