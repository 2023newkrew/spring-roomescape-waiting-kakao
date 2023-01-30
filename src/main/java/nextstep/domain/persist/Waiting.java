package nextstep.domain.persist;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Waiting {
    private Long id;
    private Schedule schedule;
    private Member member;

    public Waiting(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
    }

    public Waiting(Long id, Schedule schedule, Member member) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
