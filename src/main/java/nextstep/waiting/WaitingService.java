package nextstep.waiting;

import auth.exception.AuthErrorCode;
import auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import nextstep.exception.dataaccess.DataAccessErrorCode;
import nextstep.exception.dataaccess.DataAccessException;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.reservation.ReservationService;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.waiting.dto.response.ReservationWaitingResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WaitingService {

    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;
    private final ReservationService reservationService;

    public Long createWaiting(Member member, Long scheduleId) {
        if (member == null) {
            throw new AuthException(AuthErrorCode.INVALID_USER);
        }
        Schedule schedule = scheduleDao.findById(scheduleId)
                .orElseThrow(() -> new DataAccessException(DataAccessErrorCode.SCHEDULE_NOT_FOUND));

        Reservation newReservation = new Reservation(schedule, member, Reservation.Status.WAITING_APPROVAL);

        return reservationDao.save(newReservation);
    }

    public void cancelWaitingById(Member member, Long id) {
        reservationService.cancelById(member, id);
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
        return waitNum > 0;
    }
}
