package app.nextstep.entity;

import app.nextstep.domain.Reservation;

public class ReservationEntity {
    private Long id;
    private ScheduleEntity schedule;
    private MemberEntity member;
    private String status;

    public ReservationEntity(Long id, ScheduleEntity schedule, MemberEntity member, String status) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.status = status;
    }

    public Reservation toReservation() {
        return new Reservation(id, schedule.toSchedule(), member.toMember(), status);
    }
}
