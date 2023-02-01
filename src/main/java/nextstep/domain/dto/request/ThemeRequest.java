package nextstep.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nextstep.domain.persist.Theme;

@Getter
@AllArgsConstructor
@Builder
public class ThemeRequest {
    @Schema(description = "테마 이름")
    private String name;
    @Schema(description = "테마 설명")
    private String desc;
    @Schema(description = "테마 가격")
    private int price;

    public Theme toEntity() {
        return new Theme(
                this.name,
                this.desc,
                this.price
        );
    }
}
