package nextstep.ReservationWaiting;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.reservationwaiting.ReservationWaiting;
import nextstep.reservationwaiting.ReservationWaitingDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ReservationWaitingDaoTest {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationWaitingDao reservationWaitingDao;
    @Autowired
    private ScheduleDao scheduleDao;
    @Autowired
    private ThemeDao themeDao;

    private Member member2;
    private Member member3;

    private Schedule schedule;

    @BeforeEach
    void setUp() {
        // member 생성
        Member member1 = Member.builder().username("1").password("password").name("name").phone("phone").build();
        member2 = Member.builder().username("2").password("password").name("name").phone("phone").build();
        member3 = Member.builder().username("3").password("password").name("name").phone("phone").build();

        Long member1Id = memberDao.save(member1);
        Long member2Id = memberDao.save(member2);
        Long member3Id = memberDao.save(member3);

        member1 = Member.builder().id(member1Id).username("1").password("password").name("name").phone("phone").build();
        member2 = Member.builder().id(member2Id).username("1").password("password").name("name").phone("phone").build();
        member3 = Member.builder().id(member3Id).username("1").password("password").name("name").phone("phone").build();


        // theme 생성
        Long themeId = themeDao.save(new Theme("theme", "desc", 1000));
        Theme theme = new Theme(themeId, "theme", "desc", 1000);

        // schedule 생성
        Long sheduleId = scheduleDao.save(new Schedule(theme, LocalDate.parse("2022-08-11"), LocalTime.parse("11:00")));
        schedule = new Schedule(sheduleId, theme, LocalDate.parse("2022-08-11"), LocalTime.parse("11:00"));

        // reservation 생성
        reservationDao.save(new Reservation(schedule, member1));
    }

    @Test
    @DisplayName("예약 대기를 등록하고 순번을 조회할 때 올바른 순번 번호가 반환된다.")
    void name() {
        ReservationWaiting reservationWaiting1 = new ReservationWaiting(schedule, member2);
        ReservationWaiting reservationWaiting2 = new ReservationWaiting(schedule, member3);

        reservationWaitingDao.save(reservationWaiting1);
        reservationWaitingDao.save(reservationWaiting2);

        List<ReservationWaiting> reservationWaitingList1 = reservationWaitingDao.findByMemberId(member2.getId());
        Long result1 = reservationWaitingDao.rankBySceduleId(reservationWaitingList1.get(0).getId(), reservationWaitingList1.get(0).getSchedule().getId());

        assertThat(reservationWaitingList1.size()).isEqualTo(1);
        assertThat(result1).isEqualTo(1L);

        List<ReservationWaiting> reservationWaitingList2 = reservationWaitingDao.findByMemberId(member3.getId());
        Long result2 = reservationWaitingDao.rankBySceduleId(reservationWaitingList2.get(0).getId(), reservationWaitingList2.get(0).getSchedule().getId());

        assertThat(reservationWaitingList2.size()).isEqualTo(1);
        assertThat(result2).isEqualTo(2L);
    }
}
