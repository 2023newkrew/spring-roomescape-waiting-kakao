package nextstep.reservation.dto.response;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.schedule.Schedule;

public class ReservationResponseDto {

    private long id;
    private Schedule schedule;
    private Member member;

    private ReservationResponseDto(long id, Schedule schedule, Member member) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
    }

    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(reservation.getId(), reservation.getSchedule(), reservation.getMember());
    }

    public long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Member getMember() {
        return member;
    }

}
