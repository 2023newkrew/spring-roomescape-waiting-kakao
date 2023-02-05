package nextstep.waiting;

import lombok.RequiredArgsConstructor;
import nextstep.exception.dataaccess.DataAccessErrorCode;
import nextstep.exception.dataaccess.DataAccessException;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
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

    public Long createWaiting(Member member, Long scheduleId) {
        Schedule schedule = scheduleDao.findById(scheduleId)
                .orElseThrow(() -> new DataAccessException(DataAccessErrorCode.SCHEDULE_NOT_FOUND));

        Reservation newReservation = new Reservation(schedule, member, Reservation.Status.WAITING_APPROVAL);

        return reservationDao.save(newReservation);
    }

    public List<ReservationWaitingResponseDto> getReservationWaitingsByMember(Member member) {
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

    public boolean isWaitingReservation(Reservation reservation) {
        Long scheduleId = reservation.getSchedule().getId();
        Long waitTicketNumber = reservation.getWaitTicketNumber();
        return !isReservationNotWaiting(scheduleId, waitTicketNumber);
    }

    public boolean isReservationNotWaiting(Long scheduleId, Long waitTicketNum) {
        return !isWaitingReservation(reservationDao.getPriority(scheduleId, waitTicketNum));
    }

    private boolean isWaitingReservation(Long waitNum) {
        return waitNum > 0;
    }
}
