package nextstep.reservationwaiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationWaitingService {
    private final ReservationWaitingDao reservationWaitingDao;
    private final ScheduleDao scheduleDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ScheduleDao scheduleDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.scheduleDao = scheduleDao;
    }

    public Long create(Member member, ReservationWaitingRequest reservationWaitingRequest) {
        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId());

        ReservationWaiting newReservationWaiting = new ReservationWaiting(
                schedule,
                member.getId(),
                reservationWaitingDao.findMaxWaitNumByScheduleId(reservationWaitingRequest.getScheduleId()) + 1
        );

        return reservationWaitingDao.save(newReservationWaiting);
    }

    public List<ReservationWaiting> findAllByMemberId(Member member) {
        return reservationWaitingDao.findByMemberId(member.getId());
    }

    public void deleteById(Member member, Long id) {
        if (!reservationWaitingDao.existById(id, member.getId())) {
            throw new NullPointerException();
        }
        reservationWaitingDao.deleteById(id);
    }
}