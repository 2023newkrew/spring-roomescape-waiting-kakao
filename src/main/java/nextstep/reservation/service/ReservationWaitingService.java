package nextstep.reservation.service;

import auth.AuthenticationException;
import java.util.List;
import nextstep.member.Member;
import nextstep.reservation.dao.ReservationDao;
import nextstep.reservation.dao.ReservationWaitingDao;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationWaiting;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

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
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId()); // try -catch
        List<Reservation> reservationList = reservationDao.findByScheduleId(schedule.getId());
        if (reservationList.size() == 0) {
            reservationService.create(member, reservationRequest);
        }
        List<ReservationWaiting> reservationWaitingList = reservationWaitingDao.findByScheduleId(schedule.getId());
        reservationWaitingDao.save(new ReservationWaiting(schedule, member,
                reservationWaitingList.get(reservationWaitingList.size() - 1).getWaitingNum() + 1));
        return "Location: /reservation-waitings/" + reservationRequest.getScheduleId();
    }


    public List<ReservationWaiting> lookUp(Member member) {
        return reservationWaitingDao.findByMemberId(member.getId());
    }

    public void delete(Member member, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id);
        if (!reservationWaiting.getMember().getId().equals(member.getId())) {
            throw new AuthenticationException("User does not match");
        }
        reservationWaitingDao.deleteById(id);
    }
}
