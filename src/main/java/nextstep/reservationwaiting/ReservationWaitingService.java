package nextstep.reservationwaiting;

import auth.AuthenticationException;
import auth.UserDetails;
import java.util.List;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

@Service
public class ReservationWaitingService {
    public final ReservationWaitingDao reservationWaitingDao;
    public final ScheduleDao scheduleDao;
    public final ReservationDao reservationDao;
    public final MemberDao memberDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ScheduleDao scheduleDao, ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.scheduleDao = scheduleDao;
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public Long create(UserDetails userDetails, ReservationWaitingRequest reservationWaitingRequest) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }
        Long memberId = userDetails.getId();
        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        Long maxPriority = reservationWaitingDao.getMaxPriorityNumber(schedule);
        if (maxPriority == null) {
            maxPriority = 0L;
        }
        Long newPriority = maxPriority + 1;

        ReservationWaiting newReservationWaiting = new ReservationWaiting(
                schedule,
                memberId,
                newPriority
        );

        return reservationWaitingDao.save(newReservationWaiting);
    }

    public List<ReservationWaiting> findAllByUserDetails(UserDetails userDetails) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }
        return reservationWaitingDao.findAllByMemberId(userDetails.getId());
    }

    public int deleteById(UserDetails userDetails, Long id) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }
        return reservationWaitingDao.deleteById(userDetails.getId());
    }

    public Long findTopPriorityMemberIdBySchedule(Schedule schedule) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findTopPriorityByScheduleId(schedule.getId());
        if (reservationWaiting == null) {
            return null;
        }
        reservationWaitingDao.deleteById(reservationWaiting.getId());
        return reservationWaiting.getMemberId();
    }
}
