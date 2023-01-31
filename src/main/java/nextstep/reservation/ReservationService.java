package nextstep.reservation;

import nextstep.exception.UnauthorizedException;
import nextstep.error.ErrorCode;
import nextstep.exception.NotExistEntityException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.exception.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Long create(Member member, ReservationRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(() -> new NotExistEntityException(ErrorCode.SCHEDULE_NOT_FOUND));

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException(ErrorCode.DUPLICATE_RESERVATION);
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> findByMemberId(Member member) {
        return reservationDao.findByMemberId(member.getId());
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId)
                .orElseThrow(() -> new NotExistEntityException(ErrorCode.THEME_NOT_FOUND));

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new NotExistEntityException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!reservation.sameMember(member)) {
            throw new UnauthorizedException(ErrorCode.FORBIDDEN);
        }

        reservationDao.deleteById(id);
    }

    public boolean hasReservation(Long scheduleId) {
        return !reservationDao.findByScheduleId(scheduleId).isEmpty();
    }
}
