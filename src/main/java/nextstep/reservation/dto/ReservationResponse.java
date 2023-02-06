package nextstep.reservation.dto;

import lombok.Value;
import nextstep.member.dto.MemberResponse;
import nextstep.schedule.dto.ScheduleResponse;

@Value
public class ReservationResponse {
    Long id;
    MemberResponse member;
    ScheduleResponse schedule;
}
