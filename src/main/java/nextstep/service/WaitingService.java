package nextstep.service;

import auth.domain.persist.UserDetails;
import auth.support.AuthenticationException;
import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.request.WaitingRequest;
import nextstep.domain.dto.response.WaitingResponse;
import nextstep.domain.persist.Member;
import nextstep.domain.persist.Reservation;
import nextstep.domain.persist.Waiting;
import nextstep.repository.ReservationDao;
import nextstep.repository.ScheduleDao;
import nextstep.repository.WaitingDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaitingService {
    private static final String RESERVATION = "reservations";
    private static final String WAITING = "reservation-waitings";

    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;
    private final WaitingDao waitingDao;

    public String create(UserDetails userDetails, WaitingRequest waitingRequest) {
        Long scheduleId = waitingRequest.getScheduleId();
        List<Reservation> reservations = reservationDao.findByScheduleId(scheduleId);

        if (reservations.isEmpty()) {
            Long reservationId = reservationDao.save(new Reservation(scheduleDao.findById(scheduleId), new Member(userDetails)));
            return "/" + RESERVATION + "/" + reservationId;
        }

        Long waitingId = waitingDao.save(new Waiting(scheduleDao.findById(scheduleId), new Member(userDetails)));
        return "/" + WAITING + "/" + waitingId;
    }

    public List<WaitingResponse> findAll(Long id) {
        List<Waiting> waitings = waitingDao.findAll(id);

        return waitings.stream()
                .map(waiting -> (new WaitingResponse(waiting, getPriority(waiting))))
                .collect(Collectors.toList());
    }

    private Long getPriority(Waiting waiting) {
        return waitingDao.getPriority(waiting.getSchedule().getId(), waiting.getId());
    }

    public void deleteById(UserDetails userDetails, Long id) {
        Waiting waiting = waitingDao.findById(id);

        if (waiting == null) {
            throw new NullPointerException();
        }

        if (!waiting.sameMember(new Member(userDetails))) {
            throw new AuthenticationException();
        }

        waitingDao.deleteById(id);
    }
}
