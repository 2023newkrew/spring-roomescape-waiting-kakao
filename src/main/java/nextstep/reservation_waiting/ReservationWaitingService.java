package nextstep.reservation_waiting;

import auth.AuthenticationException;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.NonExistEntityException;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationWaitingService {
    public static final String RESERVATIONS_PATH = "/reservations/";
    public static final String RESERVATION_WAITINGS_PATH = "/reservation-waitings/";

    private final ReservationWaitingDao reservationWaitingDao;
    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;

    public URI create(Member member, ReservationWaitingRequest reservationWaitingRequest) {
        Long scheduleId = reservationWaitingRequest.getScheduleId();
        Schedule schedule = scheduleDao.findById(scheduleId);

        if (reservationDao.findByScheduleId(scheduleId).isEmpty()) {
            Reservation reservation = new Reservation(schedule, member);
            return URI.create(RESERVATIONS_PATH + reservationDao.save(reservation));
        }

        Integer nextWaitNum = getNextWaitNum(reservationWaitingDao.findAllByScheduleIdOrderByDesc(scheduleId));
        ReservationWaiting reservationWaiting = new ReservationWaiting(schedule, member, nextWaitNum);
        return URI.create(RESERVATION_WAITINGS_PATH + reservationWaitingDao.save(reservationWaiting));
    }

    private Integer getNextWaitNum(List<ReservationWaiting> reservationWaitings) {
        return reservationWaitings.isEmpty() ? 1 : reservationWaitings.get(0).getWaitNum() + 1;
    }

    public List<ReservationWaitingResponse> readMine(Member member) {
        return reservationWaitingDao.findAllByMember(member).stream()
                .map(ReservationWaitingResponse::fromDomain)
                .collect(Collectors.toList());
    }

    public void deleteById(Member member, Long id) {
        ReservationWaiting reservationWaiting =
                reservationWaitingDao.findById(id).orElseThrow(NonExistEntityException::new);

        if(!reservationWaiting.isSameMember(member)){
            throw new AuthenticationException();
        }

        reservationWaitingDao.deleteById(id);
    }
}
