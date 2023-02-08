package app.nextstep.dto;

import app.nextstep.domain.ReservationWaiting;

public class ReservationWaitingResponse {
    private Long id;
    private ScheduleResponse schedule;
    private MemberResponse member;
    private Long waitingNumber;
    public ReservationWaitingResponse(ReservationWaiting waiting) {
        this.id = waiting.getId();;
        this.schedule = new ScheduleResponse(waiting.getSchedule());
        this.member = new MemberResponse(waiting.getMember());
        this.waitingNumber = waiting.getWaitingNumber();
    }

    public Long getId() {
        return id;
    }

    public ScheduleResponse getSchedule() {
        return schedule;
    }

    public MemberResponse getMember() {
        return member;
    }

    public Long getWaitingNumber() {
        return waitingNumber;
    }
}
