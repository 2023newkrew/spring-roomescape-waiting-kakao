package nextstep.waiting;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

@RequiredArgsConstructor
@Builder
@Getter
public class Waiting {

    private final Long id;
    private final Schedule schedule;
    private final Member member;

    public boolean isCreatedBy(Member member) {
        return member != null && this.member.getId().equals(member.getId());
    }
}
