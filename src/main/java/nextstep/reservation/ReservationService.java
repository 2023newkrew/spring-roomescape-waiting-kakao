package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.dto.response.ReservationWaitingResponseDto;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    public Long create(Member member, Long scheduleId) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(scheduleId);
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public Long createWaiting(Member member, Long scheduleId) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(scheduleId);
        if (schedule == null) {
            throw new NullPointerException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public void deleteWaitingById(Member member, Long id) {
        deleteById(member, id);
    }

    public List<Reservation> getReservationsByMember(Member member) {
        if (member == null) {
            throw new AuthenticationException();
        }
        // 멤버의 모든 예약 조회
        List<Reservation> reservations = reservationDao.findByMemberId(member.getId());

        // 멤버가 가진 모든 예약의 scheduleId를 활용하여 첫 번째 순서인 예약들 조회
        List<Reservation> firstReservations = reservations.stream()
                .map(Reservation::getSchedule)
                .map(Schedule::getId)
                .map(reservationDao::findFirstByScheduleId)
                .toList();

        // 멤버가 가진 예약들 중 첫 번째 순서인 예약들을 반환
        return reservations.stream()
                .filter(firstReservations::contains)
                .collect(Collectors.toList());
    }

    public List<ReservationWaitingResponseDto> getReservationWaitingsByMember(Member member) {
        if (member == null) {
            throw new AuthenticationException();
        }
        List<Reservation> reservationsByMemberId = reservationDao.findByMemberId(member.getId());
        List<List<Reservation>> reservationsPerSchedule = reservationsByMemberId.stream()
                .map(Reservation::getSchedule)
                .map(Schedule::getId)
                .map(reservationDao::findByScheduleId)
                .collect(Collectors.toList());

        List<ReservationWaitingResponseDto> responseDto = new ArrayList<>();
        for (int i = 0; i < reservationsPerSchedule.size(); i++) {
            List<Reservation> reservationList = reservationsPerSchedule.get(i);
            reservationList.sort(Comparator.comparing(Reservation::getWaitTicketNumber));
            Reservation reservation = reservationsByMemberId.get(i);
            int waitNum = reservationList.indexOf(reservation);
            if (waitNum != 0) {
                responseDto.add(ReservationWaitingResponseDto.of(reservation, waitNum));
            }
        }
        return responseDto;
    }
}
