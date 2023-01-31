package nextstep.reservationwaiting;

import static nextstep.utils.Validator.checkFieldIsNull;

import nextstep.schedule.Schedule;

public class ReservationWaiting {
    private Long id;
    private final Schedule schedule;
    private final Long memberId;
    private final Long waitNum;

    private ReservationWaiting(Schedule schedule, Long memberId, Long waitNum) {
        checkFieldIsNull(schedule, "schedule");
        checkFieldIsNull(memberId, "memberId");
        checkFieldIsNull(waitNum, "waitNum");
        this.schedule = schedule;
        this.memberId = memberId;
        this.waitNum = waitNum;
    }

    public static ReservationWaiting giveId(ReservationWaiting reservationWaiting, Long id) {
        checkFieldIsNull(id, "id");
        reservationWaiting.id = id;
        return reservationWaiting;
    }

    public static ReservationWaitingBuilder builder() {
        return new ReservationWaitingBuilder();
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getWaitNum() {
        return waitNum;
    }

    public static class ReservationWaitingBuilder {
        private Schedule schedule;
        private Long memberId;
        private Long waitNum;

        public ReservationWaitingBuilder schedule(Schedule schedule) {
            this.schedule = schedule;
            return this;

        }

        public ReservationWaitingBuilder memberId(Long memberId) {
            this.memberId = memberId;
            return this;

        }

        public ReservationWaitingBuilder waitNum(Long waitNum) {
            this.waitNum = waitNum;
            return this;
        }

        public ReservationWaiting build() {
            return new ReservationWaiting(schedule, memberId, waitNum);
        }
    }
}
