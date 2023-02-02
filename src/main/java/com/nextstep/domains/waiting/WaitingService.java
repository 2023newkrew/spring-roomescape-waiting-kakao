package com.nextstep.domains.waiting;

import com.nextstep.domains.member.entities.MemberEntity;
import com.nextstep.domains.waiting.entities.WaitingEntity;
import com.nextstep.domains.global.exceptions.PermissionDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaitingService {
    private final WaitingDao reservationWaitingDao;

    public WaitingService(WaitingDao reservationWaitingDao) {
        this.reservationWaitingDao = reservationWaitingDao;
    }

    public Long create(MemberEntity member, Long scheduleId) {
        return reservationWaitingDao.save(member.getId(), scheduleId);
    }

    public List<WaitingEntity> getReservationWaitings(MemberEntity member) {
        return reservationWaitingDao.findByMemberId(member.getId());
    }

    public void deleteById(Long memberId, Long id) {
        WaitingEntity reservationWaiting = reservationWaitingDao.findById(id);
        if (reservationWaiting.getMember().getId() != memberId) {
            throw new PermissionDeniedException();
        }
        reservationWaitingDao.deleteById(id);
    }
}
