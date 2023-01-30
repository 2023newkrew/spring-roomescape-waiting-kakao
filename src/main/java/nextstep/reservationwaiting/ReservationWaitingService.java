package nextstep.reservationwaiting;

import java.util.List;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

@Service
public class ReservationWaitingService {
    public final ReservationWaitingDao reservationWaitingDao;
    public final ScheduleDao scheduleDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ScheduleDao scheduleDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.scheduleDao = scheduleDao;
    }

    public Long create(Long memberId, ReservationWaitingRequest reservationWaitingRequest) {
        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        Long newPriority = reservationWaitingDao.getMaxPriority(schedule) + 1;

        ReservationWaiting newReservationWaiting = new ReservationWaiting(
                schedule,
                memberId,
                newPriority
        );

        return reservationWaitingDao.save(newReservationWaiting);
    }

    public List<ReservationWaiting> findAllByMemberId(Long memberId) {
        return reservationWaitingDao.findAllByMemberId(memberId);
    }

    public int deleteById(Long memberId, Long id) {
        return reservationWaitingDao.deleteById(id);
    }
}
