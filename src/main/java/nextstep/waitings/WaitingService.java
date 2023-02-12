package nextstep.waitings;

import auth.AuthorizationException;
import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import nextstep.schedule.ScheduleService;
import nextstep.support.NotExistEntityException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaitingService {
    private final WaitingDao waitingDao;
    private final ReservationService reservationService;
    private final ScheduleService scheduleService;

    public WaitingService(final WaitingDao waitingDao, final ReservationService reservationService, final ScheduleService scheduleService) {
        this.waitingDao = waitingDao;
        this.reservationService = reservationService;
        this.scheduleService = scheduleService;
    }

    public List<WaitingResponse> findAllByMemberId(final Member member) {
        List<Waiting> waitings = waitingDao.findAllByMemberId(member.getId());
        return waitings.stream()
                .map(waiting -> Waiting.builder()
                        .id(waiting.getId())
                        .schedule(scheduleService.findById(waiting.getSchedule().getId()).orElseThrow(NotExistEntityException::new))
                        .memberId(waiting.getMemberId())
                        .build()
                )
                .map(this::toWaitingToWaitingResponse)
                .collect(Collectors.toList());
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
        Waiting waiting = waitingDao.findById(waitingId).orElseThrow(NotExistEntityException::new);

        if (waiting.getMemberId() != member.getId()) {
            // 403 Error, handled by controller
            throw new AuthorizationException();
        }
        waitingDao.deleteById(waitingId);
    }

    public WaitingResponse toWaitingToWaitingResponse(Waiting waiting) {
        return new WaitingResponse(waiting, countByScheduleId(waiting.getSchedule().getId()));
    }
}
