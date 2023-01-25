package nextstep.service;

import auth.domain.persist.UserDetails;
import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.WaitingRequest;
import nextstep.domain.persist.Member;
import nextstep.domain.persist.Reservation;
import nextstep.domain.persist.Waiting;
import nextstep.repository.ReservationDao;
import nextstep.repository.ScheduleDao;
import nextstep.repository.WaitingDao;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
}
