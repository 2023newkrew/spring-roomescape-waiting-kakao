package nextstep.service;

import auth.domain.UserDetails;
import auth.support.AuthenticationException;
import lombok.RequiredArgsConstructor;
import nextstep.controller.dto.request.WaitingRequest;
import nextstep.controller.dto.response.WaitingResponse;
import nextstep.domain.Member;
import nextstep.domain.Reservation;
import nextstep.domain.ReservationStatus;
import nextstep.domain.Waiting;
import nextstep.repository.ReservationDao;
import nextstep.repository.ScheduleDao;
import nextstep.repository.WaitingDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaitingService {
    private static final String RESERVATION = "/reservations";
    private static final String WAITING = "/reservation-waitings";

    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;
    private final WaitingDao waitingDao;

    public String create(UserDetails userDetails, WaitingRequest waitingRequest) {
        long scheduleId = waitingRequest.getScheduleId();
        List<Reservation> reservations = reservationDao.findByScheduleId(scheduleId);
        if (reservations.isEmpty()) {
            long id = reservationDao.save(new Reservation(scheduleDao.findById(scheduleId), new Member(userDetails), ReservationStatus.CREATED));
            return RESERVATION + "/" + id;
        }
        long id = waitingDao.save(new Waiting(scheduleDao.findById(scheduleId), new Member(userDetails)));
        return WAITING + "/" + id;
    }

    public List<WaitingResponse> findAll(long id) {
        // 해당 memberId가 예약한 scheduleId, waitingId 가져옴
        List<Waiting> waitings = waitingDao.findAll(id);
        // scheduleId에 해당하는 waitingId의 순번을 가져옴
        return waitings.stream()
                .map(r -> (new WaitingResponse(r, waitingDao.getPriority(r.getSchedule().getId(), r.getId()))))
                .collect(Collectors.toList());
    }

    public void deleteById(UserDetails userDetails, long id) {
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
