package nextstep.reservation.service;

import auth.AuthenticationException;
import java.util.List;
import java.util.Objects;
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

    public Long create(Member member, ReservationRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (Objects.isNull(schedule)) {
            throw new RuntimeException("해당 ID의 스케줄이 존재하지 않습니다.");
        }
        List<Reservation> reservationList = reservationDao.findByScheduleId(schedule.getId());
        if (reservationList.size() == 0) {
            reservationService.create(member, reservationRequest);
        }
        List<ReservationWaiting> reservationWaitingList = reservationWaitingDao.findByScheduleId(schedule.getId());
        long waitNum = reservationWaitingList.isEmpty() ? 1 : reservationWaitingList.get(reservationWaitingList.size() - 1).getWaitingNum() + 1;
        return reservationWaitingDao.save(new ReservationWaiting(schedule, member, waitNum));
    }


    public List<ReservationWaiting> lookUp(Member member) {
        return reservationWaitingDao.findByMemberId(member.getId());
    }

    public void delete(Member member, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id);
        if (reservationWaiting == null) {
            throw new NullPointerException();
        }
        if (!reservationWaiting.getMember().getId().equals(member.getId())) {
            throw new AuthenticationException("User does not match");
        }
        reservationWaitingDao.deleteById(id);
    }
}
