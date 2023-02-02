package nextstep.waitings;

import nextstep.schedule.Schedule;

/**
 * 예약대기(Waiting)에 관한 정보를 담는 클래스.<br><br>
 * Reservation Entity의 필드와 똑같지만, <br>
 * 추후 확장 가능성을 위하여(대기번호를 가진다거나 하는 등) 다른 Entity로 구현하였다.
 */
public class Waiting {
    private final long id;
    private final Schedule schedule;
    private final long memberId;

    public Waiting(final long id, final Schedule schedule, final long memberId) {
        this.id = id;
        this.schedule = schedule;
        this.memberId = memberId;
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

}
