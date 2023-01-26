package nextstep.waiting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nextstep.member.domain.Member;
import nextstep.schedule.domain.Schedule;

import java.util.Objects;

@AllArgsConstructor
public class Waiting {

    @Getter
    @Setter
    private Long id;

    @Getter
    private final Member member;

    @Getter
    private final Schedule schedule;

    @Getter
    private final Integer waitNum;

    public Long getMemberId() {
        if (Objects.isNull(member)) {
            return null;
        }

        return member.getId();
    }

    public Long getScheduleId() {
        if (Objects.isNull(schedule)) {
            return null;
        }

        return schedule.getId();
    }
}
