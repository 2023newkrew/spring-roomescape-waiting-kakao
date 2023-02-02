package nextstep.reservation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Reservation {

    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitTicketNumber;
    private Status status;

    public Reservation(Schedule schedule, Member member, Status status) {
        this.schedule = schedule;
        this.member = member;
        this.status = status;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Getter
    public enum Status {
        WAITING_APPROVAL("예약 미승인"),
        APPROVAL("예약 승인"),
        CANCEL("예약 취소"),
        CANCEL_WAITING("예약 취소 대기"),
        REJECT("예약 거절"),
        ;

        private final String koreanStatus;

        Status(String koreanStatus) {
            this.koreanStatus = koreanStatus;
        }
    }
}
