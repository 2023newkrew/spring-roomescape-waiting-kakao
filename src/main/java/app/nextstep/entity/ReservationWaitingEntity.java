package app.nextstep.entity;

import app.nextstep.domain.ReservationWaiting;

public class ReservationWaitingEntity extends ReservationEntity {
    private Long waitingNumber;
    public ReservationWaitingEntity(Long id, ScheduleEntity schedule, MemberEntity member, Long waitingNumber) {
        super(id, schedule, member, "WAITING");
        this.waitingNumber = waitingNumber;
    }

    public ReservationWaiting toReservationWaiting() {
        return new ReservationWaiting(super.getId(), super.getSchedule().toSchedule(), super.getMember().toMember(), waitingNumber);
    }
}
