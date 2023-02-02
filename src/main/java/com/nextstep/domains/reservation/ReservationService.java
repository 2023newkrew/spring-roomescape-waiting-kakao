package com.nextstep.domains.reservation;

import com.authorizationserver.domains.authorization.exceptions.AuthenticationException;
import com.nextstep.domains.global.exceptions.DuplicateEntityException;
import com.nextstep.domains.member.MemberDao;
import com.nextstep.domains.member.entities.MemberEntity;
import com.nextstep.domains.reservation.entities.ReservationEntity;
import com.nextstep.domains.schedule.entities.ScheduleEntity;
import com.nextstep.domains.theme.ThemeDao;
import com.nextstep.domains.theme.entities.ThemeEntity;
import com.nextstep.domains.schedule.ScheduleDao;
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

    public Long create(MemberEntity member, Long scheduleId) {
        ScheduleEntity schedule = scheduleDao.findById(scheduleId);
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<ReservationEntity> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        ReservationEntity newReservation = new ReservationEntity(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public List<ReservationEntity> findAllByThemeIdAndDate(Long themeId, String date) {
        ThemeEntity theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(MemberEntity member, Long id) {
        ReservationEntity reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public List<ReservationEntity> findAllByMemberId(Long memberId) {
        return reservationDao.findByMemberId(memberId);
    }
}
