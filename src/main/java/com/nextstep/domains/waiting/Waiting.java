package com.nextstep.domains.waiting;

import com.nextstep.domains.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.nextstep.domains.schedule.Schedule;

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
