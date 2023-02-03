package nextstep.reservation;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public class Reservation {
    private final Long id;
    @NonNull
    private final Schedule schedule;
    @NonNull
    private final Member member;

    public Reservation(Schedule schedule, Member member) {
        this(null, schedule, member);
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
