package nextstep.waiting;

import auth.AuthenticationException;
import auth.TokenMember;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingService {
    private final ScheduleDao scheduleDao;
    private final ReservationDao reservationDao;
    private final WaitingDao waitingDao;
    public String create(Member member, WaitingRequestDTO waitingRequestDTO) {
        Schedule schedule = scheduleDao.findById(waitingRequestDTO.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();}

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            Waiting newWaiting = new Waiting(
                    schedule,
                    member
            );

            return "/reservation-waitings/" + waitingDao.save(newWaiting);
        }
        Reservation newReservation = new Reservation(
                schedule,
                member
        );
        return "/reservations/" + reservationDao.save(newReservation);
    }


    public void deleteById(Member member, Long id) {
        Waiting waiting = waitingDao.findById(id);
        if (waiting == null) {
            throw new NullPointerException();
        }

        if (!waiting.isSameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public List<MyWaiting> findAllByMember(TokenMember member) {
        return waitingDao.findAllByMemberId(member.getId());
    }
}
