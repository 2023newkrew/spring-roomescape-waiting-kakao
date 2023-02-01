<<<<<<<< HEAD:src/main/java/nextstep/domain/dto/request/ReservationRequest.java
package nextstep.domain.dto.request;
========
package nextstep.domain.dto;
>>>>>>>> 59193cb (refactor: 패키지 구조 변경):src/main/java/nextstep/domain/dto/ReservationRequest.java

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {
    @Schema(description = "스케줄 아이디")
    private Long scheduleId;
}
