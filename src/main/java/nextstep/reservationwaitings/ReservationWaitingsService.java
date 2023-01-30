package nextstep.reservationwaitings;

import nextstep.member.Member;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

@Service
public class ReservationWaitingsService {

    private final ReservationWaitingsDao reservationWaitingsDao;
    private final ScheduleDao scheduleDao;

    public ReservationWaitingsService(ReservationWaitingsDao reservationWaitingsDao, ScheduleDao scheduleDao) {
        this.reservationWaitingsDao = reservationWaitingsDao;
        this.scheduleDao = scheduleDao;
    }

    public long create(Member member, ReservationWaitingRequest request) {
        Schedule schedule = scheduleDao.findById(request.getScheduleId());
        long waitNum = reservationWaitingsDao.findWaitNumByScheduleId(schedule.getId());
        ReservationWaitings reservationWaitings = new ReservationWaitings(member, schedule, waitNum);
        return reservationWaitingsDao.create(reservationWaitings);
    }

}
