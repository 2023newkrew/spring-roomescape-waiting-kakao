package nextstep.waiting;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

@Service
public class ReservationWaitingService {
    private final ReservationWaitingDao reservationWaitingDao;
    private final ScheduleDao scheduleDao;
    private final MemberDao memberDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ScheduleDao scheduleDao,
                                     MemberDao memberDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    public Long save(Long memberId, ReservationWaitingRequest reservationWaitingRequest) {
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new AuthenticationException();
        }

        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        ReservationWaiting reservationWaiting = new ReservationWaiting(
                schedule,
                member
        );

        schedule.increaseWaitingNumber();
        scheduleDao.updateWaitNum(schedule);
        return reservationWaitingDao.save(reservationWaiting);
    }
}
