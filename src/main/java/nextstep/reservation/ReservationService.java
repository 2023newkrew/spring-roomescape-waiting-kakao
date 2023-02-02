package nextstep.reservation;

import auth.ForbiddenException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.support.NotExistEntityException;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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
        Member member = memberDao.findById(memberId)
                .orElseThrow(NotExistEntityException::new);
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(NotExistEntityException::new);

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
        if (themeDao.findById(themeId).isEmpty()) {
            throw new NotExistEntityException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long memberId, Long reservationId) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(NotExistEntityException::new);

        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow(NotExistEntityException::new);

        if (!reservation.sameMember(member)) {
            throw new ForbiddenException();
        }

        reservationDao.deleteById(reservationId);
    }

    public boolean existsByScheduleId(Long scheduleId) {
        return !reservationDao.findByScheduleId(scheduleId).isEmpty();
    }

    public List<Reservation> findByMemberId(Long memberId) {
        return reservationDao.findByMemberId(memberId);
    }
}
