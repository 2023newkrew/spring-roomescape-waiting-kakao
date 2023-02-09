package nextstep.reservation.service;

import java.util.List;
import java.util.Objects;
import nextstep.common.Lock;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import nextstep.member.Member;
import nextstep.reservation.dao.ReservationDao;
import nextstep.reservation.dao.ReservationWaitingDao;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationWaiting;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservationWaitingService {
    private final ReservationWaitingDao reservationWaitingDao;
    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;
    public static final Lock reservationWaitingListLock = new Lock();

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ReservationDao reservationDao,
                                     ScheduleDao scheduleDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.reservationDao = reservationDao;
        this.scheduleDao = scheduleDao;
    }

    public Long create(Member member, ReservationRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId()).orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.SCHEDULE_NOT_FOUND);
        });
        ReservationService.reservationListLock.lock();
        List<Reservation> reservationList = reservationDao.findValidByScheduleId(schedule.getId().orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.INVALID_SCHEDULE);
        }));
        if (reservationList.isEmpty()) {
            Reservation newReservation = new Reservation(
                    schedule,
                    member
            );
            long savedId = reservationDao.save(newReservation);
            ReservationService.reservationListLock.unlock();
            return savedId;
        }
        reservationWaitingListLock.lock();
        List<ReservationWaiting> reservationWaitingList = reservationWaitingDao.findByScheduleId(schedule.getId().orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.INVALID_SCHEDULE);
        }));
        boolean isDuplicated = reservationWaitingList.stream()
                .anyMatch(reservationWaiting -> reservationWaiting.isMine(member));
        if (isDuplicated) {
            reservationWaitingListLock.unlock();
            ReservationService.reservationListLock.unlock();
            throw new RoomReservationException(ErrorCode.DUPLICATE_RESERVATION_WAITING);
        }
        long waitNum = reservationWaitingList.stream().mapToLong(ReservationWaiting::getWaitingNum).max().orElse(0) + 1;
        long savedId = reservationWaitingDao.save(new ReservationWaiting(schedule, member, waitNum));
        reservationWaitingListLock.unlock();
        ReservationService.reservationListLock.unlock();
        return savedId;
    }


    @Transactional(readOnly = true)
    public List<ReservationWaiting> lookUp(Member member) {
        return reservationWaitingDao.findByMemberId(member.getId().orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.INVALID_MEMBER);
        }));
    }

    public void delete(Member member, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id).orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.RESERVATION_WAITING_NOT_FOUND);
        });
        if (!reservationWaiting.getMember().getId().equals(member.getId())) {
            throw new RoomReservationException(ErrorCode.RESERVATION_WAITING_NOT_FOUND);
        }
        reservationWaitingDao.deleteById(id);
    }
}
