package nextstep.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaitingRequest {
    @Schema(description = "스케줄 ID")
    private Long scheduleId;
}
