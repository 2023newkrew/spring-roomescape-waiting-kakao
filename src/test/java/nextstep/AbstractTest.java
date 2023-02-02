package nextstep;

import nextstep.exception.NotExistEntityException;
import nextstep.member.Member;
import nextstep.member.MemberRequest;
import nextstep.member.MemberService;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import nextstep.role.Role;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.schedule.ScheduleRequest;
import nextstep.schedule.ScheduleService;
import nextstep.theme.Theme;
import nextstep.theme.ThemeRequest;
import nextstep.theme.ThemeService;
import nextstep.waiting.ReservationWaiting;
import nextstep.waiting.ReservationWaitingDao;
import nextstep.waiting.ReservationWaitingRequest;
import nextstep.waiting.ReservationWaitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalTime;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AbstractTest {

    private static final String MEMBER_USERNAME = "username";
    private static final String MEMBER_PASSWORD = "password";
    private static final String MEMBER_NAME = "name";
    private static final String MEMBER_PHONE = "010-1234-5678";

    private static final String THEME_NAME = "name";
    private static final String THEME_DESC = "desc";
    private static final int THEME_PRICE = 30_000;

    private Long seq = 0L;
    private LocalDate localDate = LocalDate.EPOCH;
    private LocalTime localTime = LocalTime.MIN;

    @Autowired
    MemberService memberService;
    @Autowired
    ThemeService themeService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    ScheduleDao scheduleDao;
    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationDao reservationDao;
    @Autowired
    ReservationWaitingService reservationWaitingService;
    @Autowired
    ReservationWaitingDao reservationWaitingDao;

    protected Member createNewMember(Role role) {
        seq++;

        String username = MEMBER_USERNAME + seq;
        String password = MEMBER_PASSWORD + seq;
        String name = MEMBER_NAME + seq;
        String phone = MEMBER_PHONE;
        String roleStr = role.name();

        MemberRequest memberRequest = new MemberRequest(username, password, name, phone, roleStr);

        Long memberId = memberService.create(memberRequest);

        return new Member(memberId, username, password, name, phone, roleStr);
    }

    protected Theme createNewTheme() {
        seq++;

        String name = THEME_NAME + seq;
        String desc = THEME_DESC;
        int price = THEME_PRICE;

        ThemeRequest themeRequest = new ThemeRequest(name, desc, price);
        Long themeId = themeService.create(themeRequest);

        return new Theme(themeId, name, desc, price);
    }

    protected Schedule createNewSchedule(Long themeId) {
        seq++;

        String date = localDate.plusDays(seq).toString();
        String time = localTime.plusMinutes(seq).toString();

        return createNewSchedule(themeId, date, time);
    }

    protected Schedule createNewSchedule(Long themeId, String date, String time) {
        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, date, time);
        Long scheduleId = scheduleService.create(scheduleRequest);
        return scheduleDao.findById(scheduleId).orElseThrow(IllegalStateException::new);
    }

    protected Reservation createNewReservation(Long scheduleId, Member member) {
        ReservationRequest reservationRequest = new ReservationRequest(scheduleId);
        Long reservationId = reservationService.create(member, reservationRequest);
        return reservationDao.findById(reservationId).orElseThrow(IllegalStateException::new);
    }

    protected ReservationWaiting createNewReservationWaiting(Long scheduleId, Member member) {
        ReservationWaitingRequest reservationWaitingRequest = new ReservationWaitingRequest(scheduleId);
        Long reservationWaitingId = reservationWaitingService.create(member, reservationWaitingRequest);
        return reservationWaitingDao.findById(reservationWaitingId).orElseThrow(IllegalStateException::new);
    }
}
