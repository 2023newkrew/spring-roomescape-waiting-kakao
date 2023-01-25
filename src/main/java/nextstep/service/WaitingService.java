package nextstep.service;

import auth.domain.persist.UserDetails;
import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.WaitingRequest;
import nextstep.domain.persist.Member;
import nextstep.domain.persist.Reservation;
import nextstep.repository.ReservationDao;
import nextstep.repository.ScheduleDao;
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

    public String create(UserDetails userDetails, WaitingRequest waitingRequest) {
        Long scheduleId = waitingRequest.getScheduleId();
        List<Reservation> reservations = reservationDao.findByScheduleId(scheduleId);
        if (reservations.isEmpty()) {
            return "/" + RESERVATION + "/" + reservationDao.save(new Reservation(scheduleDao.findById(scheduleId), new Member(userDetails)));
        }
    }
}
