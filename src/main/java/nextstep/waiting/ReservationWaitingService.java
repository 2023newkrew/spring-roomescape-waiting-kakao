package nextstep.waiting;

import nextstep.member.Member;
import nextstep.support.PermissionDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationWaitingService {
    private final ReservationWaitingDao reservationWaitingDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao) {
        this.reservationWaitingDao = reservationWaitingDao;
    }

    public Long create(Member member, Long scheduleId) {
        return reservationWaitingDao.save(member.getId(), scheduleId);
    }

    public List<ReservationWaiting> findByMemberId(Long memberId) {
        return reservationWaitingDao.findByMemberId(memberId);
    }

    public void deleteById(Long memberId, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id);
        if (reservationWaiting.getMember().getId() != memberId) {
            throw new PermissionDeniedException();
        }
        reservationWaitingDao.deleteById(id);
    }
}
