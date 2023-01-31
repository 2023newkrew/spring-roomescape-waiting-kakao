package nextstep.reservation.dto.response;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.schedule.Schedule;

// Reservation 관련 정보에 waitNum(대기순번)을 포함하여 돌려주기 위한 DTO
public class ReservationWaitingResponseDto {

    private long id;
    private Schedule schedule;
    private Member member;
    private long waitNum;

    private ReservationWaitingResponseDto() {
    }

    private ReservationWaitingResponseDto(long id, Schedule schedule, Member member, long waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.waitNum = waitNum;
    }

    public static ReservationWaitingResponseDto of(Reservation reservation, long waitNum) {
        return new ReservationWaitingResponseDto(reservation.getId(), reservation.getSchedule(), reservation.getMember(), waitNum);
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

    public long getWaitNum() {
        return waitNum;
    }
}
