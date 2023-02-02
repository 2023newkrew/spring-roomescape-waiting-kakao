package nextstep.reservationwaiting;

import nextstep.exception.ErrorCode;
import nextstep.exception.RoomEscapeException;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationWaitingService {
    public final ReservationDao reservationDao;
    public final ReservationWaitingDao reservationWaitingDao;
    public final ScheduleDao scheduleDao;

    public ReservationWaitingService(ReservationDao reservationDao, ReservationWaitingDao reservationWaitingDao, ScheduleDao scheduleDao) {
        this.reservationDao = reservationDao;
        this.reservationWaitingDao = reservationWaitingDao;
        this.scheduleDao = scheduleDao;
    }

    @Transactional
    public Long reserve(Member member, ReservationWaitingRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(() -> new RoomEscapeException(ErrorCode.ENTITY_NOT_EXISTS));

        LocalDateTime now = LocalDateTime.now();
        ReservationWaiting reservationWaiting = new ReservationWaiting(schedule, member, now);
        try {
            reservationDao.save(new Reservation(schedule, member));
            reservationWaiting.reserved();
        } catch (DuplicateKeyException e) {
            reservationWaiting.waiting();
        }
        return reservationWaitingDao.save(reservationWaiting);
    }

    public List<ReservationWaitingResponse> findMyReservationWaitings(Member member) {
        return reservationWaitingDao.findAllByMemberIdWithOrder(member.getId()).stream()
                .map(ReservationWaitingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public void cancelById(Member member, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id)
                .orElseThrow(() -> new RoomEscapeException(ErrorCode.ENTITY_NOT_EXISTS));

        if (!reservationWaiting.isReservedBy(member)) {
            throw new RoomEscapeException(ErrorCode.FORBIDDEN);
        }
        if (!reservationWaitingDao.cancelById(id)) {
            throw new RoomEscapeException(ErrorCode.ENTITY_NOT_EXISTS);
        }
    }
}
