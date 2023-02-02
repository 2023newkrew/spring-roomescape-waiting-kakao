package nextstep.reservation;

import lombok.*;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
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

    public boolean isSameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public void changeStatus(Status status) {
        this.status = status;
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
