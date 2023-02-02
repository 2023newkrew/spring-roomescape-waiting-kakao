package roomwaiting.nextstep.sales;


import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.schedule.Schedule;

public class Sales {
    private Long id;
    private Member member;
    private Long price;
    private Schedule schedule;

    public Sales() {
    }

    public Sales(Long id, Member member, Long price, Schedule schedule) {
        this.id = id;
        this.member = member;
        this.price = price;
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getPrice() {
        return price;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
