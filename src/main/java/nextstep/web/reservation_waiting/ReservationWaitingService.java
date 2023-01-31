package nextstep.web.reservation_waiting;

import lombok.RequiredArgsConstructor;
import nextstep.support.exception.AuthErrorCode;
import nextstep.support.exception.BusinessException;
import nextstep.support.exception.CommonErrorCode;
import nextstep.web.member.Member;
import nextstep.web.reservation.Reservation;
import nextstep.web.reservation.ReservationDao;
import nextstep.web.schedule.Schedule;
import nextstep.web.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationWaitingService {
    public static final String RESERVATIONS_PATH = "/reservations/";
    public static final String RESERVATION_WAITINGS_PATH = "/reservation-waitings/";
    private final ReservationWaitingDao reservationWaitingDao;
    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;

    public String create(Member member, ReservationWaitingRequest reservationWaitingRequest) {
        Long scheduleId = reservationWaitingRequest.getScheduleId();
        Schedule schedule = scheduleDao.findById(scheduleId);

        if (reservationDao.findByScheduleId(scheduleId).isEmpty()) {
            Reservation reservation = new Reservation(schedule, member);
            return RESERVATIONS_PATH + reservationDao.save(reservation);
        }

        Integer waitNum = getNextWaitNum(reservationWaitingDao.findAllByScheduleIdOrderByDesc(scheduleId));
        ReservationWaiting reservationWaiting = new ReservationWaiting(schedule, member, waitNum);
        return RESERVATION_WAITINGS_PATH + reservationWaitingDao.save(reservationWaiting);
    }

    private Integer getNextWaitNum(List<ReservationWaiting> reservationWaitings) {
        return reservationWaitings.isEmpty() ? 1 : reservationWaitings.get(0).getWaitNum() + 1;
    }

    public List<ReservationWaitingResponse> readMine(Member member) {
        return reservationWaitingDao.findAllByMember(member).stream()
                .map(ReservationWaitingResponse::fromDomain)
                .collect(Collectors.toList());
    }

    public void deleteById(Member member, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_EXIST_ENTITY));

        if(!reservationWaiting.getMember().equals(member)){
            throw new BusinessException(AuthErrorCode.UNAUTHORIZED_DELETE);
        }

        reservationWaitingDao.deleteById(id);
    }
}
