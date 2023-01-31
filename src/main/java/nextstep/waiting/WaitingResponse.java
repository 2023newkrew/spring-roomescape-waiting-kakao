package nextstep.waiting;

import nextstep.schedule.Schedule;

import java.util.List;
import java.util.stream.Collectors;

public class WaitingResponse {
    private Long id;
    private Schedule schedule;
    private Long waitNum;

    public WaitingResponse() {
    }

    public WaitingResponse(Long id, Schedule schedule, Long waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.waitNum = waitNum;
    }

    public static WaitingResponse of(Waiting waiting) {
        return new WaitingResponse(
                waiting.getId(),
                waiting.getSchedule(),
                waiting.getWaitNum()
        );
    }

    public static List<WaitingResponse> toList(List<Waiting> waitings) {
        return waitings.stream()
                .map(WaitingResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Long getWaitNum() {
        return waitNum;
    }
}
