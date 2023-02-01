package nextstep.waitingreservation;

import auth.AuthenticationException;
import auth.UserDetails;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaitingReservationService {
    private final ReservationDao reservationDao;
    private final WaitingReservationDao waitingReservationDao;
    private final ScheduleDao scheduleDao;
    private final MemberDao memberDao;

    public WaitingReservationService(ReservationDao reservationDao, WaitingReservationDao waitingReservationDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.waitingReservationDao = waitingReservationDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    public Long create(UserDetails userDetails, WaitingReservationRequest waitingReservationRequest) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        Member member = memberDao.findById(userDetails.getId());

        Schedule schedule = scheduleDao.findById(waitingReservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        if (reservationDao.findByScheduleId(waitingReservationRequest.getScheduleId()).isEmpty()) {
            Reservation reservation = new Reservation(schedule, member);
            return reservationDao.save(reservation);
        }

        List<WaitingReservation> waitingReservations = waitingReservationDao.findAllByScheduleId(schedule.getId());

        WaitingReservation newWaitingReservation = new WaitingReservation(
                schedule,
                member,
                (long) waitingReservations.size()
        );

        return waitingReservationDao.save(newWaitingReservation);
    }

    public List<WaitingReservationResponse> findMyWaitingReservations(UserDetails userDetails) {
        return waitingReservationDao.findAllByMemberId(userDetails.getId())
                .stream()
                .map(WaitingReservationResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteById(UserDetails userDetails, Long id) {
        WaitingReservation waitingReservation = waitingReservationDao.findById(id).orElseThrow(NullPointerException::new);
        if (!waitingReservation.sameMember(userDetails)) {
            throw new AuthenticationException();
        }
        waitingReservationDao.deleteById(id);
        waitingReservationDao.adjustWaitNumByScheduleIdAndBaseNum(
                waitingReservation.getSchedule().getId(),
                waitingReservation.getWaitNum()
        );
    }
}
