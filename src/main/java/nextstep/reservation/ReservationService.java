package nextstep.reservation;

import auth.exception.AuthErrorCode;
import auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import nextstep.exception.business.BusinessErrorCode;
import nextstep.exception.business.BusinessException;
import nextstep.exception.dataaccess.DataAccessErrorCode;
import nextstep.exception.dataaccess.DataAccessException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.dto.response.ReservationWaitingResponseDto;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationService {

    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public Long create(Member member, Long scheduleId) {
        if (member == null) {
            throw new AuthException(AuthErrorCode.INVALID_USER);
        }
        Schedule schedule = scheduleDao.findById(scheduleId)
                .orElseThrow(() -> new DataAccessException(DataAccessErrorCode.SCHEDULE_NOT_FOUND));

        List<Reservation> reservation = reservationDao.findAllByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new BusinessException(BusinessErrorCode.RESERVATION_ALREADY_EXIST_AT_THAT_TIME);
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Optional<Theme> theme = themeDao.findById(themeId);
        if (theme.isEmpty()) {
            throw new DataAccessException(DataAccessErrorCode.THEME_NOT_FOUND);
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new DataAccessException(DataAccessErrorCode.RESERVATION_NOT_FOUND));

        if (!reservation.sameMember(member)) {
            throw new BusinessException(BusinessErrorCode.DELETE_FAILED_WHEN_NOT_MY_RESERVATION);
        }

        reservationDao.deleteById(id);
    }

    public Long createWaiting(Member member, Long scheduleId) {
        if (member == null) {
            throw new AuthException(AuthErrorCode.INVALID_USER);
        }
        Schedule schedule = scheduleDao.findById(scheduleId)
                .orElseThrow(() -> new DataAccessException(DataAccessErrorCode.SCHEDULE_NOT_FOUND));

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
            throw new AuthException(AuthErrorCode.INVALID_USER);
        }
        List<Reservation> reservations = reservationDao.findByMemberId(member.getId());
        return reservations.stream()
                .filter(reservation -> {
                    Long scheduleId = reservation.getSchedule().getId();
                    Long waitTicketNumber = reservation.getWaitTicketNumber();
                    return !isWaitingReservation(reservationDao.getPriority(scheduleId, waitTicketNumber));
                })
                .collect(Collectors.toList());
    }

    public List<ReservationWaitingResponseDto> getReservationWaitingsByMember(Member member) {
        if (member == null) {
            throw new AuthException(AuthErrorCode.INVALID_USER);
        }
        List<Reservation> reservationsByMemberId = reservationDao.findByMemberId(member.getId());
        List<ReservationWaitingResponseDto> result = new ArrayList<>();
        for (Reservation reservation : reservationsByMemberId) {
            Long waitNum = reservationDao.getPriority(reservation.getSchedule().getId(), reservation.getWaitTicketNumber());
            if (isWaitingReservation(waitNum)) {
                result.add(ReservationWaitingResponseDto.of(reservation, waitNum));
            }
        }
        return result;
    }

    private static boolean isWaitingReservation(Long waitNum) {
        return waitNum != 0;
    }
}
