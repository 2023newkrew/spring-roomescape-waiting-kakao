package nextstep.waiting;

import auth.UserDetails;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@SpringBootTest
class WaitingServiceTest {

    @Autowired
    private WaitingService waitingService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private ReservationDao reservationDao;

    private final LocalDate scheduleDate = LocalDate.of(2020, 11, 10);
    private final LocalTime scheduleTime1 = LocalTime.of(12, 00);
    private final LocalTime scheduleTime2 = LocalTime.of(14, 00);

    private Member member1;
    private Member member2;
    private Theme theme;

    private Schedule schedule1;
    private Schedule schedule2;

    private UserDetails member1Details;

    @BeforeEach
    void setUp() {
        Member memberToSave = new Member("id", "pw", "name", "010-1234-5678", "MEMBER");
        Long memberId = memberDao.save(memberToSave);
        member1 = new Member(
                memberId,
                memberToSave.getUsername(),
                memberToSave.getPassword(),
                memberToSave.getName(),
                memberToSave.getPhone(),
                memberToSave.getRole()
        );


        Member member2ToSave = new Member("id", "pw", "name", "010-1234-5678", "MEMBER");
        Long member2Id = memberDao.save(member2ToSave);
        member2 = new Member(
                member2Id,
                member2ToSave.getUsername(),
                member2ToSave.getPassword(),
                member2ToSave.getName(),
                member2ToSave.getPhone(),
                member2ToSave.getRole()
        );


        Theme themeToSave = new Theme("theme-name", "theme-desc", 10000);
        Long themeId = themeDao.save(themeToSave);
        theme = new Theme(
                themeId,
                themeToSave.getName(),
                themeToSave.getDesc(),
                themeToSave.getPrice()
        );

        Schedule schedule1ToSave = new Schedule(theme, scheduleDate, scheduleTime1);
        Long schedule1Id = scheduleDao.save(schedule1ToSave);
        schedule1 = new Schedule(
                schedule1Id,
                theme,
                scheduleDate,
                scheduleTime1
        );

        Schedule schedule2ToSave = new Schedule(theme, scheduleDate, scheduleTime2);
        Long schedule2Id = scheduleDao.save(schedule2ToSave);
        schedule2 = new Schedule(
                schedule2Id,
                theme,
                scheduleDate,
                scheduleTime2
        );

        member1Details = new UserDetails(memberId);
    }

    @DisplayName("대응되는 스케줄에 Reservation이 없다면 Waiting 생성 시 Reservation으로 등록된다")
    @Test
    void waitForReservation() {
        // given
        WaitingRequest waitingRequest = new WaitingRequest(schedule1.getId());

        // when
        WaitingRegisterStatus waitingRegisterStatus = waitingService.waitForReservation(member1Details, waitingRequest);

        // then
        assertThat(waitingRegisterStatus.isRegisteredAsWaiting()).isFalse();
    }

    @DisplayName("대응되는 스케줄에 Reservation이 있다면 Waiting 생성 시 Waiting으로 등록된다")
    @Test
    void registerWaiting() {
        // given
        Reservation reservation = new Reservation(schedule1, member2);
        reservationDao.save(reservation);
        WaitingRequest waitingRequest = new WaitingRequest(schedule1.getId());

        // when
        WaitingRegisterStatus waitingRegisterStatus = waitingService.waitForReservation(member1Details, waitingRequest);

        // then
        assertThat(waitingRegisterStatus.isRegisteredAsWaiting()).isTrue();
    }

    @DisplayName("이미 대응되는 스케줄에 멤버가 예약을 해두었다면, 예약대기를 생성할 수 없다.")
    @Test
    void registerWaitingDuplication() {
        // given
        Reservation reservation = new Reservation(schedule1, member1);
        reservationDao.save(reservation);
        WaitingRequest waitingRequest = new WaitingRequest(schedule1.getId());

        // when
        assertThatCode(() -> waitingService.waitForReservation(member1Details, waitingRequest))
                .isInstanceOf(DuplicateEntityException.class);
    }

    @DisplayName("이미 대응되는 스케줄에 멤버가 예약 대기를 해두었다면, 예약 대기를 생성할 수 없다.")
    @Test
    void waitingDuplication() {
        // given
        Reservation reservation = new Reservation(schedule1, member2);
        reservationDao.save(reservation);

        WaitingRequest waitingRequest = new WaitingRequest(schedule1.getId());
        waitingService.waitForReservation(member1Details, waitingRequest);

        // when
        assertThatCode(() -> waitingService.waitForReservation(member1Details, waitingRequest))
                .isInstanceOf(DuplicateEntityException.class);
    }
}