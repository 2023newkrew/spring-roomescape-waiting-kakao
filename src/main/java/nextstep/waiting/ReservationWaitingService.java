package nextstep.waiting;

import auth.AuthorizationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.NotExistEntityException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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
        Member member = memberDao.findById(memberId)
                .orElseThrow(NotExistEntityException::new);

        Schedule schedule = scheduleDao.findAndIncreaseWaitNum(reservationWaitingRequest.getScheduleId())
                .orElseThrow(NotExistEntityException::new);

        ReservationWaiting reservationWaiting = new ReservationWaiting(
                schedule,
                member
        );

        return reservationWaitingDao.save(reservationWaiting);
    }

    public void deleteById(Long memberId, Long reservationWaitingId) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(reservationWaitingId)
                .orElseThrow(NotExistEntityException::new);

        if (!reservationWaiting.sameMember(memberId)) {
            throw new AuthorizationException();
        }

        reservationWaitingDao.deleteById(reservationWaitingId);
    }

    public List<ReservationWaiting> findByMemberId(Long memberId) {
        return reservationWaitingDao.findByMemberId(memberId);
    }
}
