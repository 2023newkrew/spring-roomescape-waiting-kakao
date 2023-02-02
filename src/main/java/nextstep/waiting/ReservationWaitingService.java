package nextstep.waiting;

import nextstep.error.ErrorCode;
import nextstep.exception.DuplicateEntityException;
import nextstep.exception.NotExistEntityException;
import nextstep.exception.UnauthorizedException;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationWaitingService {
    private final ReservationWaitingDao reservationWaitingDao;
    public final ReservationDao reservationDao;
    public final ScheduleDao scheduleDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ReservationDao reservationDao, ScheduleDao scheduleDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.reservationDao = reservationDao;
        this.scheduleDao = scheduleDao;
    }

    public Long create(Member member, ReservationWaitingRequest reservationWaitingRequest) {
        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId())
                .orElseThrow(() -> new NotExistEntityException(ErrorCode.SCHEDULE_NOT_FOUND));

        List<Reservation> reservationAndWaitings = getReservationAndWaitings(schedule.getId());

        if (hasDuplicateReservationOrWaitings(reservationAndWaitings, member)) {
            throw new DuplicateEntityException(ErrorCode.DUPLICATE_RESERVATION);
        }

        Long waitingSeq = (long) reservationAndWaitings.size();

        ReservationWaiting newReservationWaiting = new ReservationWaiting(
                new Reservation(schedule, member),
                waitingSeq
        );

        return reservationWaitingDao.save(newReservationWaiting);
    }

    public List<ReservationWaiting> findByMemberId(Member member) {
        return reservationWaitingDao.findByMemberId(member.getId());
    }

    public void deleteById(Member member, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id)
                .orElseThrow(() -> new NotExistEntityException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!reservationWaiting.checkMemberIsOwner(member)) {
            throw new UnauthorizedException(ErrorCode.FORBIDDEN);
        }

        reservationWaitingDao.deleteById(id);

        updateWaitingSeq(reservationWaiting.getScheduleId(), reservationWaiting.getWaitingSeq());
    }

    private List<Reservation> getReservationAndWaitings(Long scheduleId) {
        List<Reservation> reservationAndWaitings =
                reservationWaitingDao.findByScheduleId(scheduleId)
                        .stream()
                        .map(ReservationWaiting::getReservation)
                        .collect(Collectors.toList());

        reservationAndWaitings.addAll(reservationDao.findByScheduleId(scheduleId));

        return reservationAndWaitings;
    }

    private boolean hasDuplicateReservationOrWaitings(List<Reservation> reservationAndWaitings, Member member) {
        return reservationAndWaitings.stream()
                .anyMatch(reservation -> reservation.checkMemberIsOwner(member));
    }

    private void updateWaitingSeq(Long scheduleId, Long waitingSeq) {
        List<ReservationWaiting> reservationWaitings = reservationWaitingDao.findByScheduleId(scheduleId);

        reservationWaitings = reservationWaitings.stream()
                .filter(v -> v.getWaitingSeq() > waitingSeq)
                .collect(Collectors.toList());

        reservationWaitings.forEach(ReservationWaiting::decreaseWaitingSeq);
        reservationWaitings.forEach(reservationWaiting ->
                reservationWaitingDao.updateWaitingSeq(
                        reservationWaiting.getReservationId(), reservationWaiting.getWaitingSeq()));
    }
}
