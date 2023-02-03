package nextstep.reservationwaiting;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.NotFoundException;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ReservationWaitingService {
    private final ReservationWaitingDao reservationWaitingDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ThemeDao themeDao, ScheduleDao scheduleDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.themeDao = themeDao;
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
        ReservationWaiting waiting = reservationWaitingDao.findById(id);
        if (Objects.isNull(waiting)) {
            throw new NotFoundException();
        }
        if (!Objects.equals(waiting.getMemberId(), member.getId())) {
            throw new AuthenticationException();
        }

        reservationWaitingDao.deleteById(id);
    }
}