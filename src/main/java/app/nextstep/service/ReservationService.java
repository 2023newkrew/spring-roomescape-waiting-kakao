package app.nextstep.service;

import app.auth.support.AuthenticationException;
import app.nextstep.dao.MemberDao;
import app.nextstep.dao.ReservationDao;
import app.nextstep.dao.ScheduleDao;
import app.nextstep.dao.ThemeDao;
import app.nextstep.domain.Member;
import app.nextstep.domain.Reservation;
import app.nextstep.domain.Schedule;
import app.nextstep.domain.Theme;
import app.nextstep.support.DuplicateEntityException;
import app.nextstep.support.EntityNotFoundException;
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

    public Long create(Long memberId, Long scheduleId) {
        if (memberId == null) {
            throw new AuthenticationException();
        }
        Member member = memberDao.findById(memberId);
        Schedule schedule = scheduleDao.findById(scheduleId);
        if (schedule == null) {
            throw new EntityNotFoundException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        return reservationDao.save(scheduleId, memberId);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new EntityNotFoundException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long memberId, Long id) {
        if (memberId == null) {
            throw new AuthenticationException();
        }
        Member member = memberDao.findById(memberId);
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new EntityNotFoundException();
        }

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }
}
