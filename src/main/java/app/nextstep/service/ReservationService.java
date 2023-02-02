package app.nextstep.service;

import app.auth.support.AuthenticationException;
import app.nextstep.dao.MemberDao;
import app.nextstep.dao.ReservationDao;
import app.nextstep.domain.Reservation;
import app.nextstep.support.DuplicateEntityException;
import app.nextstep.support.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    public final ReservationDao reservationDao;

    public final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public Long create(Long scheduleId, Long memberId) {
        List<Reservation> reservation = reservationDao.findByScheduleId(scheduleId);
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        return reservationDao.save(scheduleId, memberId);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long id, Long memberId) {
        Reservation reservation = reservationDao.findById(id);

        if (reservation == null) {
            throw new EntityNotFoundException();
        }
        if (!reservation.isReservationOf(memberId)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }
}
