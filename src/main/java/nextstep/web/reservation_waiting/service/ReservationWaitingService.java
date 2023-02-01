package nextstep.web.reservation_waiting.service;

import lombok.RequiredArgsConstructor;
import nextstep.exception.AuthErrorCode;
import nextstep.exception.BusinessException;
import nextstep.exception.CommonErrorCode;
import nextstep.web.member.domain.Member;
import nextstep.web.reservation.domain.Reservation;
import nextstep.web.reservation.dao.ReservationDao;
import nextstep.web.reservation_waiting.dao.ReservationWaitingDao;
import nextstep.web.reservation_waiting.domain.ReservationWaiting;
import nextstep.web.reservation_waiting.dto.ReservationWaitingRequest;
import nextstep.web.reservation_waiting.dto.ReservationWaitingResponse;
import nextstep.web.schedule.domain.Schedule;
import nextstep.web.schedule.dao.ScheduleDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public String create(Member member, ReservationWaitingRequest reservationWaitingRequest) {
        Long scheduleId = reservationWaitingRequest.getScheduleId();
        Schedule schedule = scheduleDao.findById(scheduleId);

        if (reservationDao.findByScheduleId(scheduleId).isEmpty()) {
            Reservation reservation = new Reservation(schedule, member);
            return RESERVATIONS_PATH + reservationDao.save(reservation);
        }

        return saveReservationWaiting(schedule, member);
    }
    
    private String saveReservationWaiting(Schedule schedule, Member member) {
        Integer waitNum = getNextWaitNum(reservationWaitingDao.findAllByScheduleIdOrderByDesc(schedule.getId()));
        ReservationWaiting reservationWaiting = new ReservationWaiting(schedule, member, waitNum);
        return RESERVATION_WAITINGS_PATH + reservationWaitingDao.save(reservationWaiting);
    }

    private Integer getNextWaitNum(List<ReservationWaiting> reservationWaitings) {
        return reservationWaitings.isEmpty() ? 1 : reservationWaitings.get(0).getWaitNum() + 1;
    }

    public List<ReservationWaitingResponse> readMine(Member member) {
        List<ReservationWaitingResponse> reservationWaitings = reservationWaitingDao.findAllByMember(member).stream()
                .map(ReservationWaitingResponse::fromDomain)
                .collect(Collectors.toList());
        if (reservationWaitings.isEmpty()) {
            throw new BusinessException(CommonErrorCode.NOT_EXIST_ENTITY);
        }
        return reservationWaitings;
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
