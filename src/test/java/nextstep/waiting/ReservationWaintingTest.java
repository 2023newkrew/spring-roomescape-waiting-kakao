package nextstep.waiting;

import nextstep.member.MemberRequest;
import nextstep.member.MemberService;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleRequest;
import nextstep.schedule.ScheduleService;
import nextstep.theme.ThemeRequest;
import nextstep.theme.ThemeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ReservationWaintingTest {

    @Autowired
    private ReservationWaitingService reservationWaitingService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ThemeService themeService;

    private Long memberId;
    private Long themeId;
    private Long scheduleId;

    @BeforeEach
    void setUp() {
        memberId = memberService.create(
                new MemberRequest("username", "password", "davi", "1234", "ADMIN")
        );
        themeId = themeService.create(
                new ThemeRequest("theme", "theme", 1234)
        );
        scheduleId = scheduleService.create(
                new ScheduleRequest(themeId, "2022-08-11", "13:00")
        );
        reservationService.create(
                memberId,
                new ReservationRequest(scheduleId)
        );
    }

    @DisplayName("예약 대기 동시 요청")
    @RepeatedTest(100)
    void multipleRequest() throws InterruptedException{
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(32);

        for(int i = 0; i < 32; i++) {
            executorService.execute(()->{
                reservationWaitingService.save(
                        memberId,
                        new ReservationWaitingRequest(scheduleId)
                );
                latch.countDown();
            });
        }

        latch.await();

        List<ReservationWaiting> waitings =  reservationWaitingService.findByMemberId(memberId)
                .stream().distinct().toList();

        Assertions.assertThat(waitings.size()).isEqualTo(32);
    }
}
