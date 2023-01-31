package nextstep.reservationwaiting;

import java.util.List;
import nextstep.exception.NotFoundException;
import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

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
            throw new NotFoundException("해당 멤버의 id에 해당하는 reservation-waiting이 존재해야 합니다.", id.toString(),
                    "삭제할 대상이 존재하지 않습니다.", ReservationWaitingService.class.getSimpleName());
        }
        reservationWaitingDao.deleteById(id);
    }
}