package nextstep.service;

import auth.domain.persist.UserDetails;
import auth.support.AuthenticationException;
import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.WaitingRequest;
import nextstep.domain.dto.WaitingResponse;
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
            Long id = reservationDao.save(new Reservation(scheduleDao.findById(scheduleId), new Member(userDetails)));
            return "/" + RESERVATION + "/" + id;
        }
        Long id = waitingDao.save(new Waiting(scheduleDao.findById(scheduleId), new Member(userDetails)));
        return "/" + WAITING + "/" + id;
    }

    public List<WaitingResponse> findAll(Long id) {
        // 해당 memberId가 예약한 scheduleId, waitingId 가져옴
        List<Waiting> waitings = waitingDao.findAll(id);
        // scheduleId에 해당하는 waitingId의 순번을 가져옴
        return waitings.stream()
                .map(r -> (new WaitingResponse(r, waitingDao.getPriority(r.getSchedule().getId(), r.getId()))))
                .collect(Collectors.toList());
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
