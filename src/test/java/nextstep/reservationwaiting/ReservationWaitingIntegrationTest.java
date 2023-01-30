package nextstep.reservationwaiting;

import auth.dto.request.TokenRequest;
import auth.dto.response.TokenResponse;
import io.restassured.RestAssured;
import nextstep.domain.member.Member;
import nextstep.domain.schedule.Schedule;
import nextstep.dto.request.MemberRequest;
import nextstep.dto.request.ScheduleRequest;
import nextstep.dto.request.ThemeRequest;
import nextstep.dto.response.ReservationWaitingResponse;
import nextstep.service.MemberService;
import nextstep.service.ReservationWaitingService;
import nextstep.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ReservationWaitingIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ReservationWaitingService reservationWaitingService;

    @Test
    void 예약_대기_생성_시_대기_순번은_중복될_수_없다() throws InterruptedException {
        // given
        Long memberId = 멤버_생성();
        TokenResponse tokenResponse = 로그인();
        Long themeId = 테마_생성(tokenResponse.getAccessToken());
        Long scheduleId = 스케줄_생성(tokenResponse.getAccessToken(), themeId);

        Member member = memberService.findById(memberId);
        Schedule schedule = scheduleService.findById(scheduleId);

        int workerCount = 10;
        CountDownLatch countDownLatch = new CountDownLatch(workerCount);

        // when
        Stream.generate(() -> new Thread(new Worker(member, schedule, countDownLatch)))
                .limit(workerCount)
                .collect(Collectors.toList())
                .forEach(Thread::start);
        countDownLatch.await();

        // then
        Set<Long> waitNumSet = reservationWaitingService.findMyReservationWaitings(memberId)
                .stream()
                .map(ReservationWaitingResponse::getId)
                .collect(Collectors.toSet());

        assertThat(waitNumSet.size()).isEqualTo(workerCount);
    }

    private Long  스케줄_생성(String token, Long themeId) {
        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, "2023-01-30", "13:00");
        var scheduleResponse = given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(scheduleRequest)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] scheduleLocation = scheduleResponse.header("Location").split("/");

        return Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);
    }

    private Long 테마_생성(String token) {
        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 22000);
        var themeResponse = given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(themeRequest)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] themeLocation = themeResponse.header("Location").split("/");

        return Long.parseLong(themeLocation[themeLocation.length - 1]);
    }

    private TokenResponse 로그인() {
        TokenRequest tokenBody = new TokenRequest("username", "password");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(TokenResponse.class);
    }

    private Long 멤버_생성() {
        MemberRequest memberBody = new MemberRequest("username", "password", "name", "010-1234-5678", "ADMIN");
        var memberResponse = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] memberLocation = memberResponse.header("Location").split("/");

        return Long.parseLong(memberLocation[memberLocation.length - 1]);
    }

    class Worker implements Runnable {

        private Member member;
        private Schedule schedule;
        private CountDownLatch countDownLatch;

        public Worker(Member member, Schedule schedule, CountDownLatch countDownLatch) {
            this.member = member;
            this.schedule = schedule;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            reservationWaitingService.createReservationWaiting(member, schedule);
            countDownLatch.countDown();
        }
    }

}
