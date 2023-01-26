package nextstep.waiting.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nextstep.member.dto.MemberResponse;
import nextstep.schedule.dto.ScheduleResponse;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class WaitingResponse {

    private final Long id;

    private final MemberResponse member;

    private final ScheduleResponse schedule;

    private final Integer waitNum;
}
