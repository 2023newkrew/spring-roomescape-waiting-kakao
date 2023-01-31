package nextstep.reservation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Reservation {

    private final Long id;
    private final Schedule schedule;
    private final Member member;

    public boolean isCreatedBy(Member member) {
        return member != null && this.member.getId().equals(member.getId());
    }
}
