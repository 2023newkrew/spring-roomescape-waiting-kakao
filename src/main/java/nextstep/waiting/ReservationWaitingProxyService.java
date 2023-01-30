package nextstep.waiting;

import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import nextstep.schedule.Schedule;
import nextstep.support.DuplicateEntityException;
import org.springframework.stereotype.Service;

@Service
public class ReservationWaitingProxyService {
    private ReservationService reservationService;
    private ReservationWaitingService waitingService;

    public ReservationWaitingProxyService(ReservationService reservationService, ReservationWaitingService waitingService) {
        this.reservationService = reservationService;
        this.waitingService = waitingService;
    }

    public ReservationWaitingResponse makeReservation(Member member, Long scheduleId) {
        try {
            Long id = reservationService.create(member, scheduleId);
            return new ReservationWaitingResponse(id, false);
        }
        catch (DuplicateEntityException e) {
            Long id = waitingService.create(member, scheduleId);
            return new ReservationWaitingResponse(id, true);
        }

    }
}
