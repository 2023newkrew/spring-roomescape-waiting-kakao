package nextstep.waiting;

import nextstep.member.Member;
import nextstep.reservation.ReservationService;
import nextstep.support.DuplicateEntityException;
import nextstep.waiting.dto.ReservationWaitingCreatedResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationWaitingProxyService {
    private ReservationService reservationService;
    private ReservationWaitingService waitingService;

    public ReservationWaitingProxyService(ReservationService reservationService, ReservationWaitingService waitingService) {
        this.reservationService = reservationService;
        this.waitingService = waitingService;
    }

    public ReservationWaitingCreatedResponse makeReservation(Member member, Long scheduleId) {
        try {
            Long id = reservationService.create(member, scheduleId);
            return new ReservationWaitingCreatedResponse(id, false);
        }
        catch (DuplicateEntityException e) {
            Long id = waitingService.create(member, scheduleId);
            return new ReservationWaitingCreatedResponse(id, true);
        }
    }

    public List<ReservationWaiting> getReservationWaitings(Member member) {
        return waitingService.findByMemberId(member.getId());
    }

    public void deleteById(Long memberId, Long waitingId) {
        waitingService.deleteById(memberId, waitingId);
    }
}
