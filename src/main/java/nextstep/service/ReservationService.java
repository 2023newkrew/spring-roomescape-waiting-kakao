package nextstep.service;

import nextstep.domain.member.Member;
import nextstep.domain.member.MemberDao;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.ReservationDao;
import nextstep.dto.request.ReservationRequest;
import nextstep.dto.response.ReservationResponse;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.schedule.ScheduleDao;
import nextstep.error.ApplicationException;
import nextstep.domain.theme.Theme;
import nextstep.domain.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.error.ErrorType.*;

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

    public Long create(Long memberId, ReservationRequest reservationRequest) {
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new ApplicationException(UNAUTHORIZED_ERROR);
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new ApplicationException(SCHEDULE_NOT_FOUND);
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new ApplicationException(RESERVATION_NOT_FOUND);
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
            throw new ApplicationException(THEME_NOT_FOUND);
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long memberId, Long reservationId) {
        Member member = memberDao.findById(memberId);
        Reservation reservation = reservationDao.findById(reservationId);

        if (reservation == null) {
            throw new ApplicationException(RESERVATION_NOT_FOUND);
        }

        if (!reservation.sameMember(member)) {
            throw new ApplicationException(UNAUTHORIZED_ERROR);
        }

        reservationDao.deleteById(reservationId);
    }

    public List<ReservationResponse> findMyReservations(Long memberId) {
        return reservationDao.findByMemberId(memberId)
                .stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }

    public List<Reservation> findByScheduleId(Long scheduleId) {
        return reservationDao.findByScheduleId(scheduleId);
    }
}
