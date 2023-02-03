package roomescape.nextstep.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.AuthenticationException;
import roomescape.nextstep.member.Member;
import roomescape.nextstep.member.MemberDao;
import roomescape.nextstep.schedule.Schedule;
import roomescape.nextstep.schedule.ScheduleDao;
import roomescape.nextstep.support.exception.BusinessException;
import roomescape.nextstep.support.exception.CommonErrorCode;
import roomescape.nextstep.theme.Theme;
import roomescape.nextstep.theme.ThemeDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    @Transactional
    public Reservation create(String username, ReservationRequest reservationRequest) {
        Member member = memberDao.findByUsername(username);
        if (member == null) {
            throw new BusinessException(CommonErrorCode.NOT_EXIST_ENTITY);
        }

        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new BusinessException(CommonErrorCode.NOT_EXIST_ENTITY);
        }

        ReservationStatus status = ReservationStatus.CONFIRMED;
        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            status = ReservationStatus.WAITING;
        }

        Reservation newReservation = Reservation.builder()
                .schedule(schedule)
                .member(member)
                .status(status)
                .build();

        return reservationDao.save(newReservation);
    }

    @Transactional
    public void deleteById(String username, Long id, ReservationStatus status) {
        Reservation reservation = reservationDao.findById(id);
        Member member = memberDao.findByUsername(username);
        if (reservation == null) {
            throw new BusinessException(CommonErrorCode.NOT_EXIST_ENTITY);
        }

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        if (reservation.getStatus() != status) {
            throw new IllegalArgumentException();
        }
        reservationDao.deleteById(id);
        reservationDao.updateLatestReservationToConfirmed(reservation.getSchedule()
                .getId());
    }

    private List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new BusinessException(CommonErrorCode.NOT_EXIST_ENTITY);
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public List<Reservation> findReservationsByThemeIdAndDateAndStatus(Long themeId, String date, ReservationStatus status) {
        return findAllByThemeIdAndDate(themeId, date).stream()
                .filter(e -> e.getStatus()
                        .equals(status))
                .collect(Collectors.toList());
    }


    public List<ReservationWaiting> findWaitingReservationsByUsername(String username) {
        return reservationDao.findAllByUsername(username)
                .stream()
                .filter(e -> e.getStatus() == ReservationStatus.WAITING)
                .map(e -> new ReservationWaiting(e, reservationDao.getWaitingNumber(e.getSchedule()
                        .getId(), e.getId())))
                .collect(Collectors.toList());
    }

    public List<Reservation> findReservationsByUsername(String username) {
        return reservationDao.findAllByUsername(username)
                .stream()
                .filter(e -> e.getStatus() == ReservationStatus.CONFIRMED)
                .collect(Collectors.toList());
    }

}
