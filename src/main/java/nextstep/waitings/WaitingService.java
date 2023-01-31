package nextstep.waitings;

import auth.AuthorizationException;
import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaitingService {
    private final WaitingDao waitingDao;
    private final ReservationService reservationService;

    public WaitingService(final WaitingDao waitingDao, final ReservationService reservationService) {
        this.waitingDao = waitingDao;
        this.reservationService = reservationService;
    }

    public List<WaitingResponse> findAllByMemberId(final Member member) {
        return waitingDao.findByMemberId(member.getId()).stream()
                .map(this::toWaitingToWaitingResponse)
                .collect(Collectors.toList());
    }

    public WaitingResponse toWaitingToWaitingResponse(Waiting waiting) {
        return new WaitingResponse(waiting, countByScheduleId(waiting.getSchedule().getId()));
    }

    public String create(final Member member, final ReservationRequest reservationRequest) {
        if (!reservationService.isReserved(reservationRequest.getScheduleId())) {
            long reservationId = reservationService.create(member, reservationRequest);
            return "/reservations/" + reservationId;
        }
        else {
            long waitingId = waitingDao.save(reservationRequest.getScheduleId(), member.getId());
            return "/reservation-waitings/" + waitingId;
        }
    }

    private Long countByScheduleId(Long scheduleId) {
        return waitingDao.countByScheduleId(scheduleId);
    }

    public void delete(final Member member, final Long waitingId) {
        Waiting waiting = waitingDao.findById(waitingId);
        if (waiting == null) {
            throw new NullPointerException();
        }

        if (waiting.getMemberId() != member.getId()) {
            // 403 Error, handled by controller
            throw new AuthorizationException();
        }
        waitingDao.deleteById(waitingId);
    }
}
