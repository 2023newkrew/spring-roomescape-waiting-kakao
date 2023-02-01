package roomwaiting.nextstep.reservation.service;

import java.util.List;
import java.util.Optional;

import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.reservation.dao.ReservationWaitingDao;
import roomwaiting.nextstep.reservation.domain.Reservation;
import roomwaiting.nextstep.reservation.domain.ReservationWaiting;
import roomwaiting.nextstep.reservation.dto.ReservationRequest;
import roomwaiting.nextstep.schedule.Schedule;
import roomwaiting.nextstep.schedule.ScheduleDao;
import roomwaiting.nextstep.reservation.dao.ReservationDao;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import static roomwaiting.support.Messages.*;

@Service
public class ReservationWaitingService {
    private final ReservationWaitingDao reservationWaitingDao;
    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;
    private final ReservationService reservationService;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ReservationDao reservationDao,
                                     ScheduleDao scheduleDao, ReservationService reservationService) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.reservationDao = reservationDao;
        this.scheduleDao = scheduleDao;
        this.reservationService = reservationService;
    }

    public String create(Member member, ReservationRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId()).orElseThrow(() ->
                new NullPointerException(SCHEDULE_NOT_FOUND.getMessage() + ID + reservationRequest.getScheduleId()));
        Optional<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (reservation.isEmpty()) {
            return "/reservation/" + reservationService.create(member, reservationRequest);
        }
        List<ReservationWaiting> reservationWaitingList = reservationWaitingDao.findByScheduleId(schedule.getId());
        long waitNum = reservationWaitingList
                .stream()
                .mapToLong(ReservationWaiting::getWaitingNum)
                .max()
                .orElse(1) + 1;
        return "/reservation-waitings/" + reservationWaitingDao.save(new ReservationWaiting(schedule, member, waitNum));
    }

    public List<ReservationWaiting> lookUp(Member member) {
        return reservationWaitingDao.findByMemberId(member.getId());
    }

    public void delete(Member member, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id).orElseThrow(() ->
                new NullPointerException(RESERVATION_WAITING_NOT_FOUND.getMessage() + ID + id));
        if (!reservationWaiting.getMember().getUsername().equals(member.getUsername())) {
            throw new AuthorizationServiceException(NOT_PERMISSION_DELETE.getMessage());
        }
        reservationWaitingDao.deleteById(id);
    }
}
