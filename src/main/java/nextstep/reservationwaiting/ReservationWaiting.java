package nextstep.reservationwaiting;

import static nextstep.reservationwaiting.ReservationWaitingStatus.FINISHED;
import static nextstep.reservationwaiting.ReservationWaitingStatus.IN_PROGRESS;
import static nextstep.reservationwaiting.ReservationWaitingStatus.WAITING;
import static nextstep.utils.Validator.checkFieldIsNull;

import nextstep.exception.InvalidChangeStatusException;
import nextstep.schedule.Schedule;

public class ReservationWaiting {
    private Long id;
    private final Schedule schedule;
    private final Long memberId;
    private final Long waitNum;
    private ReservationWaitingStatus status;

    private ReservationWaiting(Schedule schedule, Long memberId, Long waitNum, ReservationWaitingStatus status) {
        this.schedule = schedule;
        this.memberId = memberId;
        this.waitNum = waitNum;
        this.status = status;
        checkFields();
    }

    public static ReservationWaiting giveId(ReservationWaiting reservationWaiting, Long id) {
        checkFieldIsNull(reservationWaiting, "reservationWaiting");
        checkFieldIsNull(id, "id");
        reservationWaiting.id = id;
        return reservationWaiting;
    }

    public void toInProgress() {
        if (status.equals(WAITING)) {
            status = IN_PROGRESS;
            return;
        }
        throw new InvalidChangeStatusException(WAITING.toString(),
                IN_PROGRESS.toString(), getClass().getSimpleName());
    }

    public void toFinished() {
        if (status.equals(IN_PROGRESS)) {
            status = FINISHED;
            return;
        }
        throw new InvalidChangeStatusException(IN_PROGRESS.toString(), FINISHED.toString(), getClass().getSimpleName());
    }

    public void toDropped() {
        status = FINISHED;
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

    public ReservationWaitingStatus getStatus() {
        return status;
    }

    public static class ReservationWaitingBuilder {

        private Schedule schedule;
        private Long memberId;
        private Long waitNum;
        private ReservationWaitingStatus status = WAITING;

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

        public ReservationWaitingBuilder status(ReservationWaitingStatus status) {
            this.status = status;
            return this;
        }

        public ReservationWaiting build() {
            return new ReservationWaiting(schedule, memberId, waitNum, status);
        }

    }

    private void checkFields() {
        checkFieldIsNull(schedule, "schedule");
        checkFieldIsNull(memberId, "memberId");
        checkFieldIsNull(waitNum, "waitNum");
        checkFieldIsNull(status, "status");
    }
}
