package nextstep.waitings;

import nextstep.schedule.Schedule;

public final class Waiting {
    private final long id;
    private final Schedule schedule;
    private final long memberId;

    public Waiting(long id, Schedule schedule, long memberId) {
        this.id = id;
        this.schedule = schedule;
        this.memberId = memberId;
    }

    public static WaitingBuilder builder() {
        return new WaitingBuilder();
    }

    public long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public long getMemberId() {
        return memberId;
    }

    public static class WaitingBuilder {
        private Long id;
        private Schedule schedule;
        private Long memberId;

        private WaitingBuilder() {
        }

        public WaitingBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public WaitingBuilder schedule(Schedule schedule) {
            this.schedule = schedule;
            return this;
        }

        public WaitingBuilder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public Waiting build() {
            return new Waiting(id, schedule, memberId);
        }
    }
}
