package app.nextstep.entity;

import app.nextstep.domain.Reservation;

public class ReservationEntity {
    private Long id;
    private ScheduleEntity schedule;
    private MemberEntity member;

    public ReservationEntity() {
    }

    public ReservationEntity(Long id, ScheduleEntity schedule, MemberEntity member) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
    }

    public Reservation toReservation() {
        return new Reservation(id, schedule.toSchedule(), member.toMember());
    }
}
